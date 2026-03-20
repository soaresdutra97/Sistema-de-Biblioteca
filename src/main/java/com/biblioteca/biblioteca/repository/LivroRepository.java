package com.biblioteca.biblioteca.repository;

import com.biblioteca.biblioteca.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository e a camada responsavel por acessar o banco de dados.
//
// Em uma arquitetura em camadas:
// - controller recebe a requisicao
// - service aplica regra de negocio
// - repository conversa com o banco
//
// Esta interface NAO contem a implementacao dos metodos.
// Mesmo assim, ela funciona porque o Spring Data JPA cria a implementacao
// automaticamente em tempo de execucao.
//
// Isso e um ponto muito importante para entender Spring:
// voce declara a interface e o framework entrega o objeto pronto.
//
// Aqui existe Inversao de Controle na pratica:
// - voce nao cria manualmente um LivroRepository
// - o Spring cria e gerencia esse objeto para voce
//
// Esse objeto criado e um bean do Spring.
// Mais adiante, quando um service precisar do repository,
// o Spring fara a injecao dessa dependencia.
public interface LivroRepository extends JpaRepository<Livro, Long> {

    // JpaRepository ja traz varios metodos prontos, como:
    // - save(...)
    // - findAll()
    // - findById(...)
    // - deleteById(...)
    // - existsById(...)
    //
    // Ou seja, so de estender JpaRepository ja ganhamos um CRUD basico.
    //
    // Mais adiante, tambem podemos declarar metodos customizados
    // apenas pelo nome, por exemplo:
    // boolean existsByIsbn(String isbn);
    //
    // O Spring Data tenta interpretar o nome do metodo e gerar a consulta.
    boolean existsByIsbn(String isbn);
}
