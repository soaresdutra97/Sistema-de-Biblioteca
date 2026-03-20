package com.biblioteca.biblioteca.dto.editora;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EditoraRequest(
    @NotBlank(message = "O nome da editora e obrigatorio.")
    @Size(max = 120, message = "O nome da editora deve ter no maximo 120 caracteres.")
    String nome
) {
}
