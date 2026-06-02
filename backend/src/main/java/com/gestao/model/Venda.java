package com.gestao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
}
