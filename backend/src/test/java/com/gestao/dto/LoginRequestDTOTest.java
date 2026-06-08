package com.gestao.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoginRequestDTOTest {

    @Test
    void setters_e_getters_loginRequest() {
        LoginRequestDTO lr = new LoginRequestDTO();
        lr.setEmail("a@b.com");
        lr.setPassword("senha123");

        assertEquals("a@b.com", lr.getEmail());
        assertEquals("senha123", lr.getPassword());
    }
}
