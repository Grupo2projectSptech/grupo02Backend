package com.gestao.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FornecedorTest {

    @Test
    void criarFornecedor_e_verificarCampos() {
        Fornecedor f = new Fornecedor(1L, "Fornecedor A", "123456", "Contato", "email@ex.com", "9999-9999", "Rua X", "Cidade", "UF", "Geral", true);

        assertEquals(1L, f.getId());
        assertEquals("Fornecedor A", f.getNome());
        assertEquals("123456", f.getCnpj());
        assertTrue(f.isAtivo());
    }

    @Test
    void ativarDesativarFornecedor() {
        Fornecedor f = new Fornecedor();
        assertTrue(f.isAtivo());
        f.setAtivo(false);
        assertFalse(f.isAtivo());
    }
}
