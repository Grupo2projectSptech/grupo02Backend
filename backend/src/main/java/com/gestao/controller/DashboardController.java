package com.gestao.controller;

import com.gestao.model.Produto;
import com.gestao.model.Venda;
import com.gestao.repository.ProdutoRepository;
import com.gestao.repository.VendaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "KPIs e gráficos do painel principal")
public class DashboardController {

    @Autowired
    private VendaRepository vendaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    // ── GET /api/dashboard ──────────────────────────────────────────────────
    @GetMapping
    @Operation(summary = "Retorna todos os dados do dashboard em uma única chamada")
    public ResponseEntity<Map<String, Object>> getDashboard(
            @RequestParam(required = false) Integer ano) {

        List<Venda> todasVendas = vendaRepository.findAll();
        List<Produto> todosProdutos = produtoRepository.findAll();

        LocalDate hoje = LocalDate.now();
        int anoFiltro = (ano != null) ? ano : hoje.getYear();

        Map<String, Object> response = new HashMap<>();
        response.put("kpis",        calcularKpis(todasVendas, hoje));
        response.put("topProdutos", calcularTopProdutos(todasVendas));
        response.put("marketplace", calcularMarketplace(todasVendas));
        response.put("evolucao",    calcularEvolucao(todasVendas, anoFiltro));
        response.put("anos",        extrairAnos(todasVendas));
        response.put("estoqueBaixo", calcularEstoqueBaixo(todosProdutos));

        return ResponseEntity.ok(response);
    }

    // ── KPIs ────────────────────────────────────────────────────────────────
    private Map<String, Object> calcularKpis(List<Venda> vendas, LocalDate hoje) {
        int mesAtual = hoje.getMonthValue();
        int anoAtual = hoje.getYear();

        List<Venda> vendasMes  = vendas.stream()
                .filter(v -> v.getData() != null
                        && v.getData().getMonthValue() == mesAtual
                        && v.getData().getYear() == anoAtual)
                .toList();

        List<Venda> vendasHoje = vendas.stream()
                .filter(v -> v.getData() != null && v.getData().equals(hoje))
                .toList();

        BigDecimal rendaMes   = soma(vendasMes, Venda::getValorVenda);
        BigDecimal custoMes   = soma(vendasMes, Venda::getCustoCheio);
        BigDecimal margemMes  = soma(vendasMes, Venda::getMargem);
        BigDecimal freteMes   = vendasMes.stream()
                .map(v -> safe(v.getMotoboy()).add(safe(v.getFreteFlex())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal lucroMesPct = rendaMes.compareTo(BigDecimal.ZERO) != 0
                ? margemMes.divide(rendaMes, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;
        BigDecimal custoPct = rendaMes.compareTo(BigDecimal.ZERO) != 0
                ? custoMes.divide(rendaMes, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;
        int qtdVendasMes = vendasMes.stream()
                .mapToInt(v -> v.getQuantidade() == null ? 0 : v.getQuantidade()).sum();
        BigDecimal custoPorEntrega = qtdVendasMes > 0
                ? freteMes.divide(BigDecimal.valueOf(qtdVendasMes), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        BigDecimal custoPorEntregaPct = rendaMes.compareTo(BigDecimal.ZERO) != 0
                ? freteMes.divide(rendaMes, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        Map<String, Object> kpis = new LinkedHashMap<>();
        kpis.put("rendaMes",            rendaMes.setScale(2, RoundingMode.HALF_UP));
        kpis.put("lucroMesPct",         lucroMesPct.setScale(2, RoundingMode.HALF_UP));
        kpis.put("margemMes",           margemMes.setScale(2, RoundingMode.HALF_UP));
        kpis.put("custoMes",            custoMes.setScale(2, RoundingMode.HALF_UP));
        kpis.put("custoPct",            custoPct.setScale(2, RoundingMode.HALF_UP));
        kpis.put("custoPorEntrega",     custoPorEntrega);
        kpis.put("custoPorEntregaPct",  custoPorEntregaPct.setScale(2, RoundingMode.HALF_UP));
        kpis.put("rendaHoje",           soma(vendasHoje, Venda::getValorVenda).setScale(2, RoundingMode.HALF_UP));
        kpis.put("vendasHoje",          vendasHoje.size());
        return kpis;
    }

    // ── Top 4 produtos por margem ────────────────────────────────────────────
    private List<Map<String, Object>> calcularTopProdutos(List<Venda> vendas) {
        Map<String, Map<String, BigDecimal>> map = new LinkedHashMap<>();

        for (Venda v : vendas) {
            String key = v.getNomeProduto() != null ? v.getNomeProduto() : "Sem nome";
            map.putIfAbsent(key, new HashMap<>(Map.of(
                    "shopee", BigDecimal.ZERO,
                    "ml",     BigDecimal.ZERO,
                    "total",  BigDecimal.ZERO
            )));
            BigDecimal margem = safe(v.getMargem());
            String tipo = v.getTipo() != null ? v.getTipo() : "";
            if (tipo.equalsIgnoreCase("Shopee"))
                map.get(key).merge("shopee", margem, BigDecimal::add);
            else if (tipo.equalsIgnoreCase("Mercado Livre"))
                map.get(key).merge("ml", margem, BigDecimal::add);
            map.get(key).merge("total", margem, BigDecimal::add);
        }

        return map.entrySet().stream()
                .sorted((a, b) -> b.getValue().get("total").compareTo(a.getValue().get("total")))
                .limit(4)
                .map(e -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("nome",   e.getKey());
                    item.put("shopee", e.getValue().get("shopee").setScale(2, RoundingMode.HALF_UP));
                    item.put("ml",     e.getValue().get("ml").setScale(2, RoundingMode.HALF_UP));
                    item.put("total",  e.getValue().get("total").setScale(2, RoundingMode.HALF_UP));
                    return item;
                })
                .collect(Collectors.toList());
    }

    // ── Marketplace (donut) ──────────────────────────────────────────────────
    private List<Map<String, Object>> calcularMarketplace(List<Venda> vendas) {
        Map<String, Integer> map = new LinkedHashMap<>();
        for (Venda v : vendas) {
            String tipo = v.getTipo() != null ? v.getTipo() : "Outro";
            map.merge(tipo, v.getQuantidade() != null ? v.getQuantidade() : 1, Integer::sum);
        }
        return map.entrySet().stream()
                .map(e -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("label", e.getKey());
                    item.put("value", e.getValue());
                    return item;
                })
                .collect(Collectors.toList());
    }

    // ── Evolução mensal de faturamento ───────────────────────────────────────
    private List<Map<String, Object>> calcularEvolucao(List<Venda> vendas, int ano) {
        String[] meses = {"JAN","FEV","MAR","ABR","MAI","JUN","JUL","AGO","SET","OUT","NOV","DEZ"};
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            final int mes = i + 1;
            BigDecimal total = vendas.stream()
                    .filter(v -> v.getData() != null
                            && v.getData().getYear() == ano
                            && v.getData().getMonthValue() == mes)
                    .map(v -> safe(v.getValorVenda()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("mes",   meses[i]);
            item.put("value", total.setScale(2, RoundingMode.HALF_UP));
            resultado.add(item);
        }
        return resultado;
    }

    // ── Anos disponíveis nas vendas ──────────────────────────────────────────
    private List<Integer> extrairAnos(List<Venda> vendas) {
        return vendas.stream()
                .filter(v -> v.getData() != null)
                .map(v -> v.getData().getYear())
                .distinct()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
    }

    // ── Produtos com estoque baixo (≤ 5) ────────────────────────────────────
    private List<Map<String, Object>> calcularEstoqueBaixo(List<Produto> produtos) {
        return produtos.stream()
                .filter(p -> p.getEstoque() != null && p.getEstoque() <= 5)
                .map(p -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("id",      p.getId());
                    item.put("nome",    p.getNome());
                    item.put("estoque", p.getEstoque());
                    return item;
                })
                .collect(Collectors.toList());
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    private BigDecimal soma(List<Venda> vendas, java.util.function.Function<Venda, BigDecimal> getter) {
        return vendas.stream()
                .map(v -> safe(getter.apply(v)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal safe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
