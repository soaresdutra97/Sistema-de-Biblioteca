package com.biblioteca.biblioteca.exception;

// Esta exception representa violacoes de regra de negocio.
//
// Diferenca importante:
// - RecursoNaoEncontradoException: algo nao existe
// - RegraDeNegocioException: algo existe, mas a operacao nao pode acontecer
//
// Exemplos de regra de negocio neste sistema:
// - livro sem quantidade disponivel nao pode ser emprestado
// - usuario inativo nao pode pegar livro
// - email ja cadastrado nao pode ser reutilizado
//
// Em APIs REST, esse tipo de erro costuma virar HTTP 400 ou 409,
// dependendo da natureza do problema.
public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String message) {
        super(message);
    }
}
