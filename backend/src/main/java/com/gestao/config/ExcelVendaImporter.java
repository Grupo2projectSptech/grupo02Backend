package com.gestao.config;

import com.gestao.model.Venda;
import com.gestao.repository.VendaRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Month;

/**
 * Importa automaticamente os dados do arquivo:
 *   src/main/resources/vendas.xlsx
 *
 * O arquivo é incluído dentro do .jar na compilação (não precisa de caminho absoluto).
 * A importação só roda se o banco estiver vazio — sem duplicatas ao reiniciar.
 *
 * Estrutura de colunas esperada (índice 0-based):
 *   0  Mês           - número do mês (ex: 12.0 = dezembro)
 *   1  Canal/Loja    - nome da loja  (ex: OUTLET SHOPEE)
 *   2  Tipo envio    - XPRESS, FLEX, ML ENVIOS …
 *   3  Produto       - código/nome do produto
 *   4  Qtd           - quantidade
 *   5  Custo Un.     - custo unitário
 *   6  (ignorado - custo total raw)
 *   7  Valor Venda   - preço de venda
 *   8  ID Pedido
 *   9  Motoboy
 *  10  Frete Flex
 *  11  (ignorado - frete diff raw)
 *  12  Frete Venda
 *  13  Tarifa
 *  14  Imposto %
 *  15  (ignorado - imposto valor raw)
 *  16  Operacional
 */
@Component
@Order(2)
public class ExcelVendaImporter implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ExcelVendaImporter.class);
    private static final String ARQUIVO = "/vendas.xlsx";

    private final VendaRepository vendaRepository;

    public ExcelVendaImporter(VendaRepository vendaRepository) {
        this.vendaRepository = vendaRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (vendaRepository.count() > 0) {
            log.info("Vendas já importadas ({} registros). Pulando leitura do Excel.", vendaRepository.count());
            return;
        }

        InputStream is = getClass().getResourceAsStream(ARQUIVO);
        if (is == null) {
            log.warn("================================================================");
            log.warn("Arquivo {} não encontrado em resources.", ARQUIVO);
            log.warn("Coloque a planilha em: backend/src/main/resources/vendas.xlsx");
            log.warn("================================================================");
            return;
        }

        log.info("Importando vendas de {}...", ARQUIVO);
        int importadas = 0;
        int ignoradas  = 0;

        try (Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isVazia(row)) { ignoradas++; continue; }

                try {
                    Venda v = mapear(row);
                    if (v != null) { vendaRepository.save(v); importadas++; }
                    else ignoradas++;
                } catch (Exception e) {
                    log.warn("Linha {} ignorada: {}", i + 1, e.getMessage());
                    ignoradas++;
                }
            }
        }

        log.info("Importação concluída: {} vendas salvas, {} linhas ignoradas.", importadas, ignoradas);
    }

    // ── Mapeamento linha → Venda ─────────────────────────────────────────────

    private Venda mapear(Row row) {
        String produto     = str(row, 3);
        Double valorVenda  = num(row, 7);

        if (produto == null || produto.isBlank() || valorVenda == null || valorVenda <= 0) {
            return null;
        }

        Venda v = new Venda();

        // Data: coluna 0 = número do mês
        Double mesRaw = num(row, 0);
        if (mesRaw != null && mesRaw >= 1 && mesRaw <= 12) {
            int mes = mesRaw.intValue();
            int ano = LocalDate.now().getYear();
            if (mes > LocalDate.now().getMonthValue()) ano--;
            v.setData(LocalDate.of(ano, Month.of(mes), 1));
        } else {
            v.setData(LocalDate.now());
        }

        v.setTipo(str(row, 1));
        v.setNomeProduto(produto);
        v.setIdPedido(str(row, 8));

        Integer qtd = toInt(row, 4);
        v.setQuantidade(qtd != null ? qtd : 1);

        v.setValorVenda(bd(valorVenda));
        v.setCustoUnidade(bd(num(row, 5)));
        v.setMotoboy(bd(num(row, 9)));
        v.setFreteFlex(bd(num(row, 10)));
        v.setFreteVenda(bd(num(row, 12)));
        v.setTarifa(bd(num(row, 13)));
        v.setImposto(bd(num(row, 14)));
        v.setOperacional(bd(num(row, 16)));

        calcularCampos(v);
        return v;
    }

    // ── Cálculos (espelha VendaService) ─────────────────────────────────────

    private void calcularCampos(Venda v) {
        BigDecimal qtd        = BigDecimal.valueOf(v.getQuantidade());
        BigDecimal custoUn    = safe(v.getCustoUnidade());
        BigDecimal freteVenda = safe(v.getFreteVenda());
        BigDecimal freteFlex  = safe(v.getFreteFlex());
        BigDecimal impPct     = safe(v.getImposto());
        BigDecimal valorVenda = safe(v.getValorVenda());
        BigDecimal motoboy    = safe(v.getMotoboy());
        BigDecimal operac     = safe(v.getOperacional());
        BigDecimal tarifa     = safe(v.getTarifa());

        BigDecimal custoTotal   = custoUn.multiply(qtd);
        BigDecimal freteDiff    = freteVenda.subtract(freteFlex);
        BigDecimal impostoValor = impPct.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP).multiply(valorVenda);
        BigDecimal custoCheio   = custoTotal.add(motoboy).add(freteFlex).add(impostoValor).add(operac).add(tarifa);
        BigDecimal margem       = valorVenda.subtract(custoCheio);
        BigDecimal margemPct    = valorVenda.compareTo(BigDecimal.ZERO) != 0
                ? margem.divide(valorVenda, 10, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        v.setCustoTotal(custoTotal.setScale(2, RoundingMode.HALF_UP));
        v.setFreteDiff(freteDiff.setScale(2, RoundingMode.HALF_UP));
        v.setImpostoValor(impostoValor.setScale(2, RoundingMode.HALF_UP));
        v.setCustoCheio(custoCheio.setScale(2, RoundingMode.HALF_UP));
        v.setMargem(margem.setScale(2, RoundingMode.HALF_UP));
        v.setMargemPct(margemPct.setScale(2, RoundingMode.HALF_UP));
    }

    // ── Helpers de célula ────────────────────────────────────────────────────

    private String str(Row row, int col) {
        Cell c = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (c == null) return null;
        return switch (c.getCellType()) {
            case STRING  -> c.getStringCellValue().trim();
            case NUMERIC -> {
                double d = c.getNumericCellValue();
                yield d == Math.floor(d) ? String.valueOf((long) d) : String.valueOf(d);
            }
            default -> null;
        };
    }

    private Double num(Row row, int col) {
        Cell c = row.getCell(col, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (c == null) return null;
        return switch (c.getCellType()) {
            case NUMERIC -> c.getNumericCellValue();
            case STRING  -> {
                try { yield Double.parseDouble(c.getStringCellValue().replace(",", ".")); }
                catch (NumberFormatException e) { yield null; }
            }
            default -> null;
        };
    }

    private Integer toInt(Row row, int col) {
        Double d = num(row, col);
        return d != null ? d.intValue() : null;
    }

    private boolean isVazia(Row row) {
        for (int c = 0; c <= 8; c++) {
            Cell cell = row.getCell(c, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null && cell.getCellType() != CellType.BLANK) return false;
        }
        return true;
    }

    private BigDecimal bd(Double val) {
        return val == null ? BigDecimal.ZERO : BigDecimal.valueOf(val).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal safe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
