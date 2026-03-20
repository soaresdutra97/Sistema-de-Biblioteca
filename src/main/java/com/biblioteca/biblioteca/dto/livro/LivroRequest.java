package com.biblioteca.biblioteca.dto.livro;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;

// DTO de entrada para criar ou atualizar um livro.
//
// Responsabilidade desta classe:
// validar o formato e a obrigatoriedade dos dados recebidos pela API.
//
// Ela nao representa a tabela do banco.
// Ela representa o contrato de entrada da API.
//
// Como estamos usando record:
// - o Java gera construtor automaticamente
// - os acessores sao gerados com o nome dos componentes, por exemplo: titulo()
// - o objeto fica imutavel, o que combina bem com DTO
public record LivroRequest(

    @NotBlank(message = "O titulo e obrigatorio.")
    @Size(max = 150, message = "O titulo deve ter no maximo 150 caracteres.")
    String titulo,

    @NotNull(message = "O id do autor e obrigatorio.")
    Long autorId,

    @NotNull(message = "O id da editora e obrigatorio.")
    Long editoraId,

    @NotEmpty(message = "Pelo menos uma categoria deve ser informada.")
    Set<Long> categoriaIds,

    @NotBlank(message = "O ISBN e obrigatorio.")
    @Size(max = 20, message = "O ISBN deve ter no maximo 20 caracteres.")
    @Pattern(regexp = "[0-9X\\-]+", message = "O ISBN deve conter apenas numeros, X ou hifen.")
    String isbn,

    @NotNull(message = "O ano de publicacao e obrigatorio.")
    @Min(value = 1, message = "O ano de publicacao deve ser maior que zero.")
    Integer anoPublicacao,

    @NotNull(message = "A quantidade disponivel e obrigatoria.")
    @Min(value = 0, message = "A quantidade disponivel nao pode ser negativa.")
    Integer quantidadeDisponivel
) {
}
