package com.gestao.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ProdutoEqualsTest {

    @Test
    void produtosComMesmosCampos_saoIguais() {
        Produto p1 = new Produto(1L, "P1", "D", new BigDecimal("2.00"), 10, "C", "C1", "un", true, null);
        Produto p2 = new Produto(1L, "P1", "D", new BigDecimal("2.00"), 10, "C", "C1", "un", true, null);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
