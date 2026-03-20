package com.biblioteca.biblioteca.enums;

// Status financeiro da multa.
//
// Essa separacao em enum deixa o dominio mais expressivo
// do que um simples booleano "paga / nao paga".
public enum StatusMulta {
    PENDENTE,
    PAGA,
    CANCELADA
}
