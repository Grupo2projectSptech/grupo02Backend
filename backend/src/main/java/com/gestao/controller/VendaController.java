package com.gestao.controller;

import com.gestao.model.Venda;
import com.gestao.service.VendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
=======
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
>>>>>>> 605613bc96c70ce98af6cc7a02dc9786f2984173
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

<<<<<<< HEAD
import java.time.LocalDate;
=======
>>>>>>> 605613bc96c70ce98af6cc7a02dc9786f2984173
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
<<<<<<< HEAD
@Tag(name = "Vendas", description = "Gerenciamento de vendas do marketplace")
=======
@Tag(name = "Vendas", description = "Gerenciamento de vendas")
>>>>>>> 605613bc96c70ce98af6cc7a02dc9786f2984173
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
<<<<<<< HEAD
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Venda encontrada"),
        @ApiResponse(responseCode = "404", description = "Venda não encontrada")
=======
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Venda encontrada"),
            @ApiResponse(responseCode = "404", description = "Venda não encontrada")
>>>>>>> 605613bc96c70ce98af6cc7a02dc9786f2984173
    })
    public ResponseEntity<Venda> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

<<<<<<< HEAD
    @GetMapping("/periodo")
    @Operation(summary = "Lista vendas por período", description = "Filtra vendas entre duas datas (formato: yyyy-MM-dd)")
    public ResponseEntity<List<Venda>> findByPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(service.findByPeriodo(inicio, fim));
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Lista vendas por canal/loja", description = "Ex: OUTLET SHOPEE, OUTLET ML")
    public ResponseEntity<List<Venda>> findByTipo(@PathVariable String tipo) {
        return ResponseEntity.ok(service.findByTipo(tipo));
    }

    @GetMapping("/produto")
    @Operation(summary = "Busca vendas pelo nome do produto")
    public ResponseEntity<List<Venda>> findByProduto(@RequestParam String nome) {
        return ResponseEntity.ok(service.findByProduto(nome));
    }

    @PostMapping
    @Operation(summary = "Cadastra uma nova venda")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Venda criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Venda> create(@RequestBody Venda venda) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(venda));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma venda existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Venda atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Venda não encontrada")
    })
    public ResponseEntity<Venda> update(@PathVariable Long id, @RequestBody Venda venda) {
        return ResponseEntity.ok(service.update(id, venda));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove uma venda")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Venda removida com sucesso"),
        @ApiResponse(responseCode = "404", description = "Venda não encontrada")
=======
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
>>>>>>> 605613bc96c70ce98af6cc7a02dc9786f2984173
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
