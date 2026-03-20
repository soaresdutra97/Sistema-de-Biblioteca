package com.biblioteca.biblioteca.dto.exemplar;

import com.biblioteca.biblioteca.enums.StatusExemplar;

public record ExemplarResponse(
    Long id,
    String codigoPatrimonio,
    Long livroId,
    String tituloLivro,
    StatusExemplar status,
    String localizacao
) {
}
