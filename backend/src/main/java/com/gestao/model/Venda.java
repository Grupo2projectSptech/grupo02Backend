package com.gestao.model;

import jakarta.persistence.*;
<<<<<<< HEAD
=======
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
>>>>>>> 605613bc96c70ce98af6cc7a02dc9786f2984173
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

<<<<<<< HEAD
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
=======
    @NotNull(message = "Data é obrigatória")
    @Column(nullable = false)
    private LocalDate data;

    @NotBlank(message = "Nome do produto é obrigatório")
    @Column(nullable = false)
    private String nomeProduto;

    private String tipo;       // Shopee, Mercado Livre, Manual, Outro

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser maior que zero")
    @Column(nullable = false)
    private Integer quantidade;

    @NotNull(message = "Valor de venda é obrigatório")
    @Positive(message = "Valor de venda deve ser maior que zero")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorVenda;

    private String idPedido;

    @Column(precision = 15, scale = 2)
    private BigDecimal custoUnidade = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal motoboy = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal freteFlex = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal freteVenda = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal tarifa = BigDecimal.ZERO;

    @Column(precision = 5, scale = 2)
    private BigDecimal imposto = BigDecimal.ZERO;   // percentual

    @Column(precision = 15, scale = 2)
    private BigDecimal operacional = BigDecimal.ZERO;

    // ── Campos calculados (persistidos para facilitar relatórios) ──
    @Column(precision = 15, scale = 2)
    private BigDecimal custoTotal = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal freteDiff = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal impostoValor = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal custoCheio = BigDecimal.ZERO;

    @Column(precision = 15, scale = 2)
    private BigDecimal margem = BigDecimal.ZERO;

    @Column(precision = 5, scale = 2)
    private BigDecimal margemPct = BigDecimal.ZERO;

    // Referência opcional ao produto cadastrado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id")
    private Produto produto;
>>>>>>> 605613bc96c70ce98af6cc7a02dc9786f2984173
}
