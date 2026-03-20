package com.biblioteca.biblioteca.entity;

import com.biblioteca.biblioteca.enums.StatusMulta;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Multa representa a penalidade financeira gerada por atraso na devolucao.
//
// Em vez de guardar apenas um número dentro de Emprestimo,
// modelamos multa como entidade propria.
//
// Isso e melhor porque:
// - a multa tem ciclo de vida proprio
// - pode ficar pendente, paga ou cancelada
// - pode ter data de geracao e data de pagamento
// - facilita futuras evolucoes financeiras
@Entity
@Table(name = "multas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "emprestimo")
public class Multa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cada multa nasce vinculada a um unico emprestimo.
    // Nesta versao, consideramos no maximo uma multa por emprestimo.
    @OneToOne
    @JoinColumn(name = "emprestimo_id", nullable = false, unique = true)
    private Emprestimo emprestimo;

    @Column(name = "dias_atraso", nullable = false)
    private Integer diasAtraso;

    @Column(name = "valor", nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusMulta status;

    @Column(name = "data_geracao", nullable = false)
    private LocalDate dataGeracao;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;
}
