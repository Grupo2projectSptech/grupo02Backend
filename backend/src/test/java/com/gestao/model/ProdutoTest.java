package com.gestao.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ProdutoTest {

    @Test
    void criarProduto_comConstrutorDefault_e_setters() {
        Produto p = new Produto();
        p.setId(2L);
        p.setNome("Lapis");
        p.setPreco(new BigDecimal("0.50"));

        assertEquals(2L, p.getId());
        assertEquals("Lapis", p.getNome());
        assertEquals(new BigDecimal("0.50"), p.getPreco());
    }

    @Test
    void produto_ativoPorPadrao_true() {
        Produto p = new Produto();
        assertTrue(p.isAtivo());
    }

    @Test
    void codigoInterno_e_unidade_podemSerAtribuidos() {
        Produto p = new Produto();
        p.setCodigoInterno("C-100");
        p.setUnidade("kg");

        assertEquals("C-100", p.getCodigoInterno());
        assertEquals("kg", p.getUnidade());
    }
}
