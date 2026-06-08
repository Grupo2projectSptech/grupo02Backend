package com.gestao.service;

import com.gestao.exception.ResourceNotFoundException;
import com.gestao.model.Venda;
<<<<<<< HEAD
=======
import com.gestao.observer.VendaEventPublisher;
>>>>>>> 605613bc96c70ce98af6cc7a02dc9786f2984173
import com.gestao.repository.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
<<<<<<< HEAD
import java.time.LocalDate;
=======
>>>>>>> 605613bc96c70ce98af6cc7a02dc9786f2984173
import java.util.List;

@Service
public class VendaService {

    @Autowired
    private VendaRepository repository;

<<<<<<< HEAD
=======
    @Autowired
    private VendaEventPublisher eventPublisher;   // ← Publisher injetado

>>>>>>> 605613bc96c70ce98af6cc7a02dc9786f2984173
    public List<Venda> findAll() {
        return repository.findAll();
    }

    public Venda findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda não encontrada com id: " + id));
    }

<<<<<<< HEAD
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
=======
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
>>>>>>> 605613bc96c70ce98af6cc7a02dc9786f2984173
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Venda não encontrada com id: " + id);
        }
        repository.deleteById(id);
    }

<<<<<<< HEAD
    // ── Lógica de cálculo ────────────────────────────────────────────────────

    private void calcularCampos(Venda v) {
        BigDecimal qtd        = BigDecimal.valueOf(v.getQuantidade() != null ? v.getQuantidade() : 1);
=======
    // ── Lógica de cálculo espelhando o frontend ──────────────────────────────
    private void calcularCampos(Venda v) {
        BigDecimal qtd        = toBD(v.getQuantidade());
>>>>>>> 605613bc96c70ce98af6cc7a02dc9786f2984173
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
<<<<<<< HEAD
=======

    private BigDecimal toBD(Integer v) {
        return v == null ? BigDecimal.ZERO : BigDecimal.valueOf(v);
    }
>>>>>>> 605613bc96c70ce98af6cc7a02dc9786f2984173
}
