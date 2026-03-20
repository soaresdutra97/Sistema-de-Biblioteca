package com.biblioteca.biblioteca.dto.emprestimo;

import java.time.LocalDate;

// DTO de saida do emprestimo.
//
// Aqui escolhemos devolver:
// - ids das entidades relacionadas
// - alguns dados descritivos uteis para o cliente
// - datas e status da operacao
//
// Esse desenho pode evoluir conforme a necessidade da API.
public record EmprestimoResponse(
    Long id,
    Long exemplarId,
    String codigoPatrimonioExemplar,
    Long livroId,
    String tituloLivro,
    Long usuarioId,
    String nomeUsuario,
    LocalDate dataEmprestimo,
    LocalDate dataPrevistaDevolucao,
    LocalDate dataDevolucaoReal,
    Boolean ativo
) {
}
