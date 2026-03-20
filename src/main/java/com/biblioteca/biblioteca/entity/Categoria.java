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

// Categoria representa a classificacao de um livro.
//
// Exemplos:
// - Tecnologia
// - Ficcao
// - Historia
// - Romance
//
// Um livro pode pertencer a varias categorias.
// E uma categoria pode estar associada a varios livros.
// Por isso, mais adiante, Livro vai se relacionar com Categoria em ManyToMany.
@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true, length = 80)
    private String nome;
}
