package com.gestao.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterRequestDTOTest {

    @Test
    void defaultRole_isUser() {
        RegisterRequestDTO r = new RegisterRequestDTO();
        assertEquals("USER", r.getRole());
        r.setRole("ADMIN");
        assertEquals("ADMIN", r.getRole());
    }
}
