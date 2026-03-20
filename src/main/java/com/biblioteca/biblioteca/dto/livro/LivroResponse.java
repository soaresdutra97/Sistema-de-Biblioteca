package com.biblioteca.biblioteca.dto.livro;

import java.util.Set;

// DTO de saida de Livro.
//
// Responsabilidade:
// definir exatamente o que a API devolve ao cliente.
//
// Isso evita expor a entity diretamente e desacopla a resposta HTTP
// da estrutura interna do banco e do JPA.
public record LivroResponse(
    Long id,
    String titulo,
    Long autorId,
    String autorNome,
    Long editoraId,
    String editoraNome,
    Set<String> categorias,
    String isbn,
    Integer anoPublicacao,
    Integer quantidadeDisponivel
) {
}
