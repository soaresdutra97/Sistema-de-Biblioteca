package com.biblioteca.biblioteca.exception;

// Exception significa "condicao excepcional".
//
// Em Java, usamos exceptions para sinalizar que algo deu errado
// ou que o fluxo normal da operacao nao pode continuar.
//
// Exemplo deste projeto:
// o usuario pediu um livro pelo id 10, mas esse livro nao existe.
// Nao faz sentido continuar a operacao normalmente.
// Entao lancamos uma exception.
//
// Esta classe representa especificamente o caso:
// "o recurso procurado nao foi encontrado".
//
// Em uma API REST, isso costuma virar HTTP 404.
//
// Estamos estendendo RuntimeException.
// Isso significa que esta e uma unchecked exception.
//
// Para APIs Spring, esse costuma ser o caminho mais pratico para erros
// de negocio e de fluxo da aplicacao.
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String message) {
        super(message);
    }
}
