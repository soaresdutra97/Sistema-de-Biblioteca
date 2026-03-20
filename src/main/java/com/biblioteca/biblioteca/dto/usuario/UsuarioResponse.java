package com.biblioteca.biblioteca.dto.usuario;

// DTO de saida de usuario.
//
// Note que aqui estamos controlando explicitamente o retorno da API.
// Isso se torna ainda mais importante quando a entity cresce,
// ganha relacionamentos ou campos que nao devem ser expostos.
public record UsuarioResponse(
    Long id,
    String nome,
    String email,
    String documento,
    Boolean ativo
) {
}