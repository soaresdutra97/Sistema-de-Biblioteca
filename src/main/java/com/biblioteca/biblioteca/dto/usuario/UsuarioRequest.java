package com.biblioteca.biblioteca.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// DTO de entrada de usuario.
//
// Aqui e o lugar adequado para validacoes de formato, como:
// - email valido
// - documento com 11 digitos
// - campos obrigatorios
//
// Isso respeita melhor a responsabilidade unica:
// o DTO valida o formato de entrada
// o service valida a regra de negocio
public record UsuarioRequest(

    @NotBlank(message = "O nome e obrigatorio.")
    @Size(max = 120, message = "O nome deve ter no maximo 120 caracteres.")
    String nome,

    @NotBlank(message = "O email e obrigatorio.")
    @Email(message = "O email informado nao e valido.")
    @Size(max = 150, message = "O email deve ter no maximo 150 caracteres.")
    String email,

    @NotBlank(message = "O documento e obrigatorio.")
    @Pattern(regexp = "\\d{11}", message = "O documento deve conter exatamente 11 digitos.")
    String documento,

    @NotNull(message = "O campo ativo e obrigatorio.")
    Boolean ativo
) {
}
