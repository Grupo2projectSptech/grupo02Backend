package com.gestao.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExceptionTest {

    @Test
    void businessException_hasMessage() {
        BusinessException ex = new BusinessException("Erro de negócio");
        assertEquals("Erro de negócio", ex.getMessage());
    }

    @Test
    void resourceNotFoundException_hasMessage() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Recurso não encontrado");
        assertEquals("Recurso não encontrado", ex.getMessage());
    }
}
