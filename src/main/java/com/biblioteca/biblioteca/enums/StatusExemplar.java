package com.biblioteca.biblioteca.enums;

// Este enum representa o estado operacional de um exemplar fisico.
//
// Agora o sistema deixa de pensar apenas em "quantidade de livros"
// e passa a enxergar cada copia fisica individualmente.
//
// Isso torna o dominio mais realista:
// a biblioteca empresta um exemplar especifico, nao uma abstracao generica do livro.
public enum StatusExemplar {
    DISPONIVEL,
    EMPRESTADO,
    DANIFICADO,
    EXTRAVIADO,
    MANUTENCAO
}
