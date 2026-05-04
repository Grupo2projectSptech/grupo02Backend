package com.gestao.controller;

import com.gestao.model.Produto;
import com.gestao.service.ProdutoService;
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
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "Gerenciamento de produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping
    @Operation(summary = "Lista todos os produtos", description = "Retorna uma lista com todos os produtos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de produtos obtida com sucesso")
    public ResponseEntity<List<Produto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtém um produto por ID", description = "Retorna os detalhes de um produto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto encontrado"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Produto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/fornecedor/{fornecedorId}")
    @Operation(summary = "Lista produtos por fornecedor", description = "Retorna todos os produtos de um fornecedor específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos obtida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Fornecedor não encontrado")
    })
    public ResponseEntity<List<Produto>> findByFornecedor(@PathVariable Long fornecedorId) {
        return ResponseEntity.ok(service.findByFornecedor(fornecedorId));
    }

    @GetMapping("/empresa/{empresaId}")
    @Operation(summary = "Lista produtos por empresa", description = "Retorna todos os produtos de uma empresa específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de produtos obtida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empresa não encontrada")
    })
    public ResponseEntity<List<Produto>> findByEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(service.findByEmpresa(empresaId));
    }

    @PostMapping
    @Operation(summary = "Cria um novo produto", description = "Cadastra um novo produto no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Produto> create(@Valid @RequestBody Produto produto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(produto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um produto", description = "Atualiza as informações de um produto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Produto> update(@PathVariable Long id, @Valid @RequestBody Produto produto) {
        return ResponseEntity.ok(service.update(id, produto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um produto", description = "Remove um produto do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
