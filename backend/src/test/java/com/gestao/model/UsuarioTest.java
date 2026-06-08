package com.gestao.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    @Test
    void criarUsuario_e_verificarCampos() {
        Usuario u = new Usuario(1L, "Nome", "user@example.com", "senha", "ADMIN");
        assertEquals(1L, u.getId());
        assertEquals("Nome", u.getName());
        assertEquals("user@example.com", u.getEmail());
        assertEquals("senha", u.getPassword());
    }

    @Test
    void atualizarRole_funciona() {
        Usuario u = new Usuario();
        u.setRole("USER");
        assertEquals("USER", u.getRole());
        u.setRole("ADMIN");
        assertEquals("ADMIN", u.getRole());
    }
}
