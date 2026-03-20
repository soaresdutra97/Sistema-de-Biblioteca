package com.biblioteca.biblioteca.repository;

import com.biblioteca.biblioteca.entity.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

// Este repository acessa os dados de emprestimo.
//
// Ele sera especialmente importante porque Emprestimo e uma entidade
// que concentra a "movimentacao" do sistema.
//
// Em projetos reais, repositories de entidades transacionais como essa
// costumam ganhar consultas mais especificas, por exemplo:
// - buscar emprestimos ativos
// - buscar emprestimos por usuario
// - buscar emprestimos atrasados
// - verificar se um livro ja esta emprestado
//
// Tudo isso pode ser feito mais adiante com:
// - query methods
// - @Query
// - JPQL
// - consultas nativas, quando realmente necessario
//
// De novo, o ponto central aqui e:
// esta interface vira um bean gerenciado pelo Spring,
// mesmo sem voce escrever "new EmprestimoRepository()".
public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    // Exemplos de consultas que podem aparecer futuramente:
    // List<Emprestimo> findByAtivoTrue();
    // List<Emprestimo> findByUsuarioId(Long usuarioId);
    // boolean existsByLivroIdAndAtivoTrue(Long livroId);
    //
    // O Spring Data JPA consegue criar muitas consultas so pelo nome do metodo,
    // desde que ele siga os padroes reconhecidos pelo framework.
    boolean existsByExemplarIdAndAtivoTrue(Long exemplarId);
}
