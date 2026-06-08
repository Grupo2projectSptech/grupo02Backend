package com.gestao.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Dados básicos ────────────────────────────────────────────────────────
    private LocalDate data;

    /** Nome da loja / canal (ex: OUTLET SHOPEE, OUTLET ML) */
    private String tipo;

    /** Código ou nome do produto */
    private String nomeProduto;

    private String idPedido;

    private Integer quantidade;

    // ── Valores de entrada ───────────────────────────────────────────────────
    @Column(precision = 15, scale = 2)
    private BigDecimal valorVenda;

    @Column(precision = 15, scale = 2)
    private BigDecimal custoUnidade;

    @Column(precision = 15, scale = 2)
    private BigDecimal motoboy;

    @Column(precision = 15, scale = 2)
    private BigDecimal freteFlex;

    @Column(precision = 15, scale = 2)
    private BigDecimal freteVenda;

    @Column(precision = 15, scale = 2)
    private BigDecimal tarifa;

    /** Percentual de imposto (ex: 6.0 = 6%) */
    @Column(precision = 10, scale = 4)
    private BigDecimal imposto;

    @Column(precision = 15, scale = 2)
    private BigDecimal operacional;

    // ── Campos calculados ────────────────────────────────────────────────────
    @Column(precision = 15, scale = 2)
    private BigDecimal custoTotal;

    @Column(precision = 15, scale = 2)
    private BigDecimal freteDiff;

    @Column(precision = 15, scale = 2)
    private BigDecimal impostoValor;

    @Column(precision = 15, scale = 2)
    private BigDecimal custoCheio;

    @Column(precision = 15, scale = 2)
    private BigDecimal margem;

    /** Margem percentual (ex: 23.5 = 23,5%) */
    @Column(precision = 10, scale = 4)
    private BigDecimal margemPct;
}
