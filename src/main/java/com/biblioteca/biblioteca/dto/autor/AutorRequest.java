package com.biblioteca.biblioteca.dto.autor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AutorRequest(
    @NotBlank(message = "O nome do autor e obrigatorio.")
    @Size(max = 120, message = "O nome do autor deve ter no maximo 120 caracteres.")
    String nome
) {
}
