package com.gestao.controller;

import com.gestao.model.Empresa;
import com.gestao.service.EmpresaService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/empresas")
@Tag(name = "Empresas", description = "Gerenciamento de empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService service;

    @GetMapping
    @Operation(summary = "Lista todas as empresas", description = "Retorna uma lista com todas as empresas cadastradas")
    @ApiResponse(responseCode = "200", description = "Lista de empresas obtida com sucesso")
    public ResponseEntity<List<Empresa>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtém uma empresa por ID", description = "Retorna os detalhes de uma empresa específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa encontrada"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    public ResponseEntity<Empresa> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Cria uma nova empresa", description = "Cadastra uma nova empresa no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empresa criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Empresa> create(@Valid @RequestBody Empresa empresa) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(empresa));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma empresa", description = "Atualiza as informações de uma empresa existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Empresa> update(@PathVariable Long id, @Valid @RequestBody Empresa empresa) {
        return ResponseEntity.ok(service.update(id, empresa));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma empresa", description = "Remove uma empresa do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Empresa deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
