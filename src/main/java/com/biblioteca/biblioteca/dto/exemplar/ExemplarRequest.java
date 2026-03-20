package com.biblioteca.biblioteca.dto.exemplar;

import com.biblioteca.biblioteca.enums.StatusExemplar;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// DTO de entrada de exemplar.
//
// Observe como ele recebe o id do livro e nao um objeto Livro inteiro.
// Esse padrao continua valendo:
// a API trafega ids de referencia, e o service resolve as entidades reais.
public record ExemplarRequest(
    @NotBlank(message = "O codigo patrimonial e obrigatorio.")
    @Size(max = 50, message = "O codigo patrimonial deve ter no maximo 50 caracteres.")
    String codigoPatrimonio,

    @NotNull(message = "O id do livro e obrigatorio.")
    Long livroId,

    @NotNull(message = "O status do exemplar e obrigatorio.")
    StatusExemplar status,

    String localizacao
) {
}
