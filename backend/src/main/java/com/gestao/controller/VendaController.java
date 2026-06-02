package com.gestao.controller;

import com.gestao.model.Venda;
import com.gestao.service.VendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@Tag(name = "Vendas", description = "Gerenciamento de vendas")
public class VendaController {

    @Autowired
    private VendaService service;

    @GetMapping
    @Operation(summary = "Lista todas as vendas")
    @ApiResponse(responseCode = "200", description = "Lista obtida com sucesso")
    public ResponseEntity<List<Venda>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtém uma venda por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda encontrada"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada")
    })
    public ResponseEntity<Venda> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    @Operation(summary = "Registra uma nova venda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Venda criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Venda> create(@Valid @RequestBody Venda venda) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(venda));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancela/remove uma venda")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Venda removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
