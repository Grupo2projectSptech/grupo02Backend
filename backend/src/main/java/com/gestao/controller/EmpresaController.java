package com.gestao.controller;

import com.gestao.model.Empresa;
import com.gestao.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService service;

    @GetMapping
    public ResponseEntity<List<Empresa>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Empresa> create(@Valid @RequestBody Empresa empresa) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(empresa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Empresa> update(@PathVariable Long id, @Valid @RequestBody Empresa empresa) {
        return ResponseEntity.ok(service.update(id, empresa));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
