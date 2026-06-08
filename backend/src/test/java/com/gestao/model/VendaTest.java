package com.gestao.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class VendaTest {

    @Test
    void venda_padrao_temBigDecimalZero() {
        Venda v = new Venda();
        assertEquals(BigDecimal.ZERO, v.getCustoUnidade());
        assertEquals(BigDecimal.ZERO, v.getFreteVenda());
        assertEquals(BigDecimal.ZERO, v.getMargemPct());
    }

    @Test
    void criarVenda_e_verificarCampos() {
        Venda v = new Venda();
        v.setId(1L);
        v.setData(LocalDate.now());
        v.setNomeProduto("Produto X");
        v.setTipo("Shopee");
        v.setQuantidade(2);
        v.setValorVenda(new BigDecimal("100.00"));
        v.setIdPedido("PED123");

        assertEquals(1L, v.getId());
        assertEquals("Produto X", v.getNomeProduto());
        assertEquals(2, v.getQuantidade());
        assertEquals(new BigDecimal("100.00"), v.getValorVenda());
    }

    @Test
    void vincularProduto_aVenda() {
        Produto p = new Produto(5L, "Produto Y", null, new BigDecimal("10.0"), 5, null, null, null, true, null);
        Venda v = new Venda();
        v.setProduto(p);
        assertNotNull(v.getProduto());
        assertEquals(5L, v.getProduto().getId());
    }

    @Test
    void vender_quantidadeAtualizadaEsperada() {
        Venda v = new Venda();
        v.setQuantidade(3);
        assertEquals(3, v.getQuantidade());
        v.setQuantidade(v.getQuantidade() - 1);
        assertEquals(2, v.getQuantidade());
    }
}
