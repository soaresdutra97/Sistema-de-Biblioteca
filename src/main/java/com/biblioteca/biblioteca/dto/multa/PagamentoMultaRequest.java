package com.biblioteca.biblioteca.dto.multa;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

// DTO especifico para registrar o pagamento da multa.
public record PagamentoMultaRequest(
    @NotNull(message = "A data de pagamento e obrigatoria.")
    LocalDate dataPagamento
) {
}
