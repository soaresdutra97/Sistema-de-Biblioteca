package com.biblioteca.biblioteca.dto.multa;

import com.biblioteca.biblioteca.enums.StatusMulta;
import java.math.BigDecimal;
import java.time.LocalDate;

// DTO de saida da multa.
//
// Ele entrega os dados financeiros e tambem referencia o contexto
// de emprestimo e usuario para facilitar consultas da API.
public record MultaResponse(
    Long id,
    Long emprestimoId,
    Long usuarioId,
    String nomeUsuario,
    Integer diasAtraso,
    BigDecimal valor,
    StatusMulta status,
    LocalDate dataGeracao,
    LocalDate dataPagamento
) {
}
