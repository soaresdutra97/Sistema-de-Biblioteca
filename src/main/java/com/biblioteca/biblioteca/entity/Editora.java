package com.biblioteca.biblioteca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Editora representa a empresa ou organizacao responsavel
// pela publicacao do livro.
//
// Assim como Autor, ela deixa de ser apenas um texto solto
// para virar parte estruturada do dominio.
//
// Isso permite evolucoes futuras, como:
// - cidade
// - pais
// - site
// - data de fundacao
@Entity
@Table(name = "editoras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Editora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true, length = 120)
    private String nome;
}
