package com.gestao.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AuthResponseDTOTest {

    @Test
    void construtor_e_getters() {
        AuthResponseDTO dto = new AuthResponseDTO("tok", 10L, "Nome", "user@example.com", "USER");
        assertEquals("tok", dto.getToken());
        assertEquals(10L, dto.getId());
        assertEquals("Nome", dto.getName());
        assertEquals("user@example.com", dto.getUsername());
        assertEquals("USER", dto.getRole());
    }

    @Test
    void setters_funcionam() {
        AuthResponseDTO dto = new AuthResponseDTO();
        dto.setToken("abc");
        dto.setId(5L);
        dto.setName("X");
        dto.setUsername("u@x");
        dto.setRole("ADMIN");

        assertEquals("abc", dto.getToken());
        assertEquals(5L, dto.getId());
        assertEquals("ADMIN", dto.getRole());
    }
}
