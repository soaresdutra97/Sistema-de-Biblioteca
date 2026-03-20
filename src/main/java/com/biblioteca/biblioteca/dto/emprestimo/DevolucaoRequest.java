package com.biblioteca.biblioteca.dto.emprestimo;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

// DTO especifico para a acao de devolver um livro.
//
// Separar essa operacao costuma ser uma boa pratica quando:
// - a acao tem significado de negocio proprio
// - a validacao e diferente da criacao de emprestimo
// - a API fica mais explicita e mais facil de entender
//
// Em vez de reutilizar EmprestimoRequest para tudo,
// criamos um contrato proprio para o caso de uso de devolucao.
public record DevolucaoRequest(

    @NotNull(message = "A data de devolucao e obrigatoria.")
    LocalDate dataDevolucaoReal
) {
}
