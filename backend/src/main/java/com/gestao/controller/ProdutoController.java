package com.gestao.controller;

import com.gestao.model.Produto;
import com.gestao.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping
    public ResponseEntity<List<Produto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<List<Produto>> findByFornecedor(@PathVariable Long fornecedorId) {
        return ResponseEntity.ok(service.findByFornecedor(fornecedorId));
    }

    @GetMapping("/empresa/{empresaId}")
    public ResponseEntity<List<Produto>> findByEmpresa(@PathVariable Long empresaId) {
        return ResponseEntity.ok(service.findByEmpresa(empresaId));
    }

    @PostMapping
    public ResponseEntity<Produto> create(@Valid @RequestBody Produto produto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(produto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> update(@PathVariable Long id, @Valid @RequestBody Produto produto) {
        return ResponseEntity.ok(service.update(id, produto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
