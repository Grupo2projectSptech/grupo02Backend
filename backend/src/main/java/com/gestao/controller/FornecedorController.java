package com.gestao.controller;

import com.gestao.model.Fornecedor;
import com.gestao.service.FornecedorService;
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
@RequestMapping("/api/fornecedores")
@Tag(name = "Fornecedores", description = "Gerenciamento de fornecedores")
public class FornecedorController {

    @Autowired
    private FornecedorService service;

    @GetMapping
    @Operation(summary = "Lista todos os fornecedores", description = "Retorna uma lista com todos os fornecedores cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de fornecedores obtida com sucesso")
    public ResponseEntity<List<Fornecedor>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/ativos")
    @Operation(summary = "Lista fornecedores ativos", description = "Retorna uma lista com todos os fornecedores ativos")
    @ApiResponse(responseCode = "200", description = "Lista de fornecedores ativos obtida com sucesso")
    public ResponseEntity<List<Fornecedor>> findAtivos() {
        return ResponseEntity.ok(service.findAtivos());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtém um fornecedor por ID", description = "Retorna os detalhes de um fornecedor específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor encontrado"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    public ResponseEntity<Fornecedor> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Cria um novo fornecedor", description = "Cadastra um novo fornecedor no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fornecedor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Fornecedor> create(@Valid @RequestBody Fornecedor fornecedor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(fornecedor));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um fornecedor", description = "Atualiza as informações de um fornecedor existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fornecedor atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Fornecedor> update(@PathVariable Long id, @Valid @RequestBody Fornecedor fornecedor) {
        return ResponseEntity.ok(service.update(id, fornecedor));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um fornecedor", description = "Remove um fornecedor do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fornecedor deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
