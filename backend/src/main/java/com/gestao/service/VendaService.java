package com.gestao.service;

import com.gestao.exception.ResourceNotFoundException;
import com.gestao.model.Venda;
import com.gestao.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class VendaService {

    @Autowired
    private VendaRepository repository;

    public List<Venda> findAll() {
        return repository.findAll();
    }

    public Venda findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada com id: " + id));
    }

    public List<Venda> findByPeriodo(LocalDate inicio, LocalDate fim) {
        return repository.findByDataBetween(inicio, fim);
    }

    public List<Venda> findByTipo(String tipo) {
        return repository.findByTipo(tipo);
    }

    public List<Venda> findByProduto(String nomeProduto) {
        return repository.findByNomeProdutoContainingIgnoreCase(nomeProduto);
    }

    public Venda save(Venda venda) {
        calcularCampos(venda);
        return repository.save(venda);
    }

    public Venda update(Long id, Venda data) {
        Venda venda = findById(id);
        venda.setData(data.getData());
        venda.setTipo(data.getTipo());
        venda.setNomeProduto(data.getNomeProduto());
        venda.setIdPedido(data.getIdPedido());
        venda.setQuantidade(data.getQuantidade());
        venda.setValorVenda(data.getValorVenda());
        venda.setCustoUnidade(data.getCustoUnidade());
        venda.setMotoboy(data.getMotoboy());
        venda.setFreteFlex(data.getFreteFlex());
        venda.setFreteVenda(data.getFreteVenda());
        venda.setTarifa(data.getTarifa());
        venda.setImposto(data.getImposto());
        venda.setOperacional(data.getOperacional());
        calcularCampos(venda);
        return repository.save(venda);
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Venda não encontrada com id: " + id);
        }
        repository.deleteById(id);
    }

    // ── Lógica de cálculo ────────────────────────────────────────────────────

    private void calcularCampos(Venda v) {
        BigDecimal qtd        = BigDecimal.valueOf(v.getQuantidade() != null ? v.getQuantidade() : 1);
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
        BigDecimal impostoValor = impPct
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .multiply(valorVenda);
        BigDecimal custoCheio   = custoTotal.add(motoboy).add(freteFlex)
                                            .add(impostoValor).add(operac).add(tarifa);
        BigDecimal margem       = valorVenda.subtract(custoCheio);
        BigDecimal margemPct    = valorVenda.compareTo(BigDecimal.ZERO) != 0
                ? margem.divide(valorVenda, 10, RoundingMode.HALF_UP)
                         .multiply(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        v.setCustoTotal(custoTotal.setScale(2, RoundingMode.HALF_UP));
        v.setFreteDiff(freteDiff.setScale(2, RoundingMode.HALF_UP));
        v.setImpostoValor(impostoValor.setScale(2, RoundingMode.HALF_UP));
        v.setCustoCheio(custoCheio.setScale(2, RoundingMode.HALF_UP));
        v.setMargem(margem.setScale(2, RoundingMode.HALF_UP));
        v.setMargemPct(margemPct.setScale(2, RoundingMode.HALF_UP));
    }

    private BigDecimal safe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
