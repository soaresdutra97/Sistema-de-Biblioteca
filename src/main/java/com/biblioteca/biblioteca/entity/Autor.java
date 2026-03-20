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

// Autor passa a ser uma entidade propria do dominio.
//
// Antes, o nome do autor era apenas um texto solto dentro de Livro.
// Isso funciona em sistemas pequenos, mas limita o crescimento do dominio.
//
// Ao transformar Autor em entidade:
// - podemos reutilizar o mesmo autor em varios livros
// - evitamos repeticao de texto
// - abrimos caminho para evolucoes futuras, como biografia, nacionalidade,
//   data de nascimento ou lista de livros publicados
@Entity
@Table(name = "autores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, unique = true, length = 120)
    private String nome;
}
