package com.biblioteca.biblioteca.entity;

import com.biblioteca.biblioteca.enums.StatusExemplar;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Exemplar representa uma copia fisica de um livro.
//
// Essa e uma evolucao importante do dominio:
// - Livro representa a obra bibliografica
// - Exemplar representa cada unidade fisica existente no acervo
//
// Isso permite responder perguntas reais de biblioteca, como:
// - qual copia foi emprestada?
// - quantos exemplares existem?
// - qual exemplar esta danificado ou extraviado?
@Entity
@Table(name = "exemplares")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "livro")
public class Exemplar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Codigo patrimonial ou identificador unico do exemplar.
    // Em bibliotecas reais, isso costuma ser o "tombo", etiqueta ou codigo interno.
    @Column(name = "codigo_patrimonio", nullable = false, unique = true, length = 50)
    private String codigoPatrimonio;

    // Cada exemplar pertence a um unico livro.
    // Ja um livro pode ter muitos exemplares.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    // O status indica se este exemplar pode ser emprestado ou nao.
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusExemplar status;

    // Campo opcional para evolucao futura:
    // prateleira, setor, sala, observacoes etc.
    @Column(name = "localizacao", length = 100)
    private String localizacao;
}