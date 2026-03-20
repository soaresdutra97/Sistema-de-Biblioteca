package com.biblioteca.biblioteca.exception;

import java.time.LocalDateTime;

// Esta classe representa o corpo padrao de erro devolvido pela API.
//
// Em vez de devolver erros desorganizados ou depender do formato padrao bruto
// do framework, criamos uma estrutura previsivel para o cliente.
//
// Isso e uma boa pratica porque:
// - padroniza respostas de erro
// - facilita debug no frontend e em testes
// - deixa a API mais profissional
//
// Aqui usamos record porque esta classe e apenas transporte de dados.
public record ErroResposta(
    LocalDateTime timestamp,
    Integer status,
    String error,
    String message,
    String path
) {
}
