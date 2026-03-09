package com.gestao.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Razão social é obrigatória")
    @Column(nullable = false)
    private String razaoSocial;

    @NotBlank(message = "CNPJ é obrigatório")
    @Column(nullable = false, unique = true)
    private String cnpj;

    private String nomeFantasia;

    @NotBlank(message = "Email é obrigatório")
    @Column(nullable = false)
    private String email;

    private String telefone;
    private String endereco;
    private String cidade;
    private String estado;
    private String cep;
    private String segmento;
    private boolean ativo = true;
}
