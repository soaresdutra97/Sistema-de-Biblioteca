package com.biblioteca.biblioteca.dto.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
    @NotBlank(message = "O nome da categoria e obrigatorio.")
    @Size(max = 80, message = "O nome da categoria deve ter no maximo 80 caracteres.")
    String nome
) {
}
