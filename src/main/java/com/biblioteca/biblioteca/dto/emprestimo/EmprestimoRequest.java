package com.biblioteca.biblioteca.dto.emprestimo;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

// DTO de entrada para registrar um novo emprestimo.
//
// Aqui nao enviamos um objeto Livro inteiro nem um Usuario inteiro.
// Enviamos apenas os ids necessarios para a operacao.
//
// Isso e comum em APIs:
// a requisicao traz as referencias essenciais
// e o service busca as entidades reais no banco.
//
// Mesmo com Exemplar no dominio, continuamos recebendo livroId aqui.
// A regra de negocio ficara responsavel por escolher um exemplar disponivel
// daquele livro.
public record EmprestimoRequest(

    @NotNull(message = "O id do livro e obrigatorio.")
    Long livroId,

    @NotNull(message = "O id do usuario e obrigatorio.")
    Long usuarioId,

    @NotNull(message = "A data prevista de devolucao e obrigatoria.")
    @FutureOrPresent(message = "A data prevista de devolucao nao pode estar no passado.")
    LocalDate dataPrevistaDevolucao
) {
}
