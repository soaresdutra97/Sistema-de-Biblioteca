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

// Esta classe representa o usuario da biblioteca.
//
// No dominio do projeto, o usuario e quem pode pegar livros emprestados.
// Portanto, essa entidade vai se relacionar com Emprestimo mais adiante.
//
// Assim como em Livro:
// - a classe vira tabela
// - os atributos viram colunas
// - os objetos viram registros no banco
//
// Mais para frente, poderiamos evoluir essa modelagem para incluir:
// - endereco
// - telefone
// - status do usuario
// - limite de emprestimos simultaneos
// - data de cadastro
//
// Neste primeiro momento, vamos manter a modelagem simples e suficiente
// para um CRUD didatico e para o fluxo de emprestimo.
@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Usuario {

    // Identificador unico do usuario no banco.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nome do usuario.
    // nullable = false impede nulo no banco.
    // length define um limite de tamanho para a coluna.
    @Column(name = "nome", nullable = false, length = 120)
    private String nome;

    // Email costuma ser um dado importante de identificacao e contato.
    // unique = true ajuda a evitar usuarios duplicados com o mesmo email.
    //
    // Mais adiante, validacoes como formato de email devem ficar em DTOs
    // com Bean Validation, e nao somente na entity.
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    // Documento simples para estudo.
    // Em um sistema real, poderiamos ter CPF, matricula ou outro identificador.
    @Column(name = "documento", nullable = false, unique = true, length = 20)
    private String documento;

    // Campo que representa se o usuario esta apto a realizar emprestimos.
    // Esse tipo de dado sera util quando chegarmos na camada de service,
    // porque a regra de negocio pode impedir emprestimos de usuarios inativos.
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    // Lombok esta gerando automaticamente:
    // - getters
    // - setters
    // - construtor vazio
    // - construtor com todos os atributos
    // - toString
    //
    // Observacao futura importante:
    // quando criarmos relacionamentos com Emprestimo, talvez seja melhor
    // controlar com mais cuidado o toString para evitar logs exagerados.
}
