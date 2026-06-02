package com.gestao.service;

import com.gestao.exception.ResourceNotFoundException;
import com.gestao.model.Venda;
import com.gestao.observer.VendaEventPublisher;
import com.gestao.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class VendaService {

    @Autowired
    private VendaRepository repository;

    @Autowired
    private VendaEventPublisher eventPublisher;   // ← Publisher injetado

    public List<Venda> findAll() {
        return repository.findAll();
    }

    public Venda findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada com id: " + id));
    }

    /**
     * Persiste a venda, calcula os campos derivados e notifica os observers.
     * Ordem: calcular → salvar → notificar (observers recebem a venda com id).
     */
    public Venda save(Venda venda) {
        calcularCampos(venda);
        Venda salva = repository.save(venda);

        // ── Padrão Observer: notifica todos os observers registrados ──────────
        eventPublisher.notificar(salva);

        return salva;
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Venda não encontrada com id: " + id);
        }
        repository.deleteById(id);
    }

    // ── Lógica de cálculo espelhando o frontend ──────────────────────────────
    private void calcularCampos(Venda v) {
        BigDecimal qtd        = toBD(v.getQuantidade());
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

    private BigDecimal toBD(Integer v) {
        return v == null ? BigDecimal.ZERO : BigDecimal.valueOf(v);
    }
}
