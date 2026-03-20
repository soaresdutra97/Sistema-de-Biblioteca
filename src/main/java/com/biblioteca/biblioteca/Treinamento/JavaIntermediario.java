package com.biblioteca.treinamento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class JavaIntermediario {

    public static void main(String[] args) {
        exemploArray();
        exemploList();
        exemploSet();
        exemploMap();
        exemploWrapperEAutoboxing();
        exemploGenerics();
        exemploExceptions();
        exemploLambda();
        exemploStream();
        exemploOptional();
    }

    private static void exemploArray() {
        System.out.println("=== Array ===");

        // Array tem tamanho fixo.
        // Ele e bom quando voce sabe exatamente quantos elementos precisa guardar.
        String[] linguagens = {"Java", "Python", "JavaScript"};

        System.out.println("Primeira linguagem: " + linguagens[0]);

        for (int i = 0; i < linguagens.length; i++) {
            System.out.println("Indice " + i + ": " + linguagens[i]);
        }

        System.out.println();
    }

    private static void exemploList() {
        System.out.println("=== List ===");

        // List e uma colecao que cresce dinamicamente.
        // E muito usada quando a quantidade de elementos pode variar.
        List<String> livros = new ArrayList<>();
        livros.add("Clean Code");
        livros.add("Effective Java");
        livros.add("Spring in Action");

        System.out.println("Lista completa: " + livros);
        System.out.println("Segundo livro: " + livros.get(1));

        for (String livro : livros) {
            System.out.println("Livro: " + livro);
        }

        System.out.println();
    }

    private static void exemploSet() {
        System.out.println("=== Set ===");

        // Set nao permite elementos duplicados.
        // Ele e util quando voce quer garantir unicidade.
        Set<String> categorias = new HashSet<>();
        categorias.add("Java");
        categorias.add("Spring");
        categorias.add("Java");

        System.out.println("Categorias sem repeticao: " + categorias);
        System.out.println();
    }

    private static void exemploMap() {
        System.out.println("=== Map ===");

        // Map armazena pares chave-valor.
        // Pense nele como uma estrutura para buscar um valor pela chave.
        Map<String, Integer> estoque = new HashMap<>();
        estoque.put("Livro Java", 10);
        estoque.put("Livro Spring", 5);
        estoque.put("Livro SQL", 8);

        System.out.println("Estoque de Livro Spring: " + estoque.get("Livro Spring"));

        for (Map.Entry<String, Integer> item : estoque.entrySet()) {
            System.out.println(item.getKey() + " -> " + item.getValue());
        }

        System.out.println();
    }

    private static void exemploWrapperEAutoboxing() {
        System.out.println("=== Wrapper e Autoboxing ===");

        // Tipos primitivos como int nao sao objetos.
        // Quando precisamos trabalhar com colecoes, usamos classes wrapper como Integer.
        int numeroPrimitivo = 10;
        Integer numeroObjeto = numeroPrimitivo; // autoboxing

        Integer outroNumeroObjeto = 20;
        int soma = numeroObjeto + outroNumeroObjeto; // unboxing

        System.out.println("Numero primitivo: " + numeroPrimitivo);
        System.out.println("Numero objeto: " + numeroObjeto);
        System.out.println("Soma: " + soma);
        System.out.println();
    }

    private static void exemploGenerics() {
        System.out.println("=== Generics ===");

        // Generics permitem definir o tipo que uma colecao ou classe vai aceitar.
        // Isso traz seguranca de tipo e evita casts desnecessarios.
        Caixa<String> caixaDeTexto = new Caixa<>();
        caixaDeTexto.guardar("Java Generics");

        System.out.println("Conteudo da caixa: " + caixaDeTexto.pegar());
        System.out.println();
    }

    private static void exemploExceptions() {
        System.out.println("=== Exceptions ===");

        // Exceptions representam erros durante a execucao.
        // O try/catch permite tratar o problema sem derrubar o programa.
        try {
            int resultado = dividir(10, 0);
            System.out.println("Resultado: " + resultado);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro tratado: " + e.getMessage());
        }

        System.out.println();
    }

    private static void exemploLambda() {
        System.out.println("=== Lambda ===");

        // Lambda e uma forma curta de escrever comportamento.
        // Ela e muito usada com interfaces funcionais e colecoes.
        List<String> nomes = Arrays.asList("Ana", "Carlos", "Beatriz");
        nomes.forEach(nome -> System.out.println("Nome: " + nome));

        System.out.println();
    }

    private static void exemploStream() {
        System.out.println("=== Stream ===");

        // Stream permite processar colecoes de forma declarativa.
        // Aqui filtramos nomes com mais de 4 letras e convertimos para maiusculo.
        List<String> nomes = Arrays.asList("Ana", "Carlos", "Beatriz", "Joao");

        List<String> nomesFiltrados = nomes.stream()
            .filter(nome -> nome.length() > 4)
            .map(String::toUpperCase)
            .toList();

        System.out.println("Resultado da stream: " + nomesFiltrados);
        System.out.println();
    }

    private static void exemploOptional() {
        System.out.println("=== Optional ===");

        // Optional ajuda a representar a possibilidade de ausencia de valor.
        // Ele reduz a chance de NullPointerException quando usado com criterio.
        Optional<String> nomeEncontrado = buscarNomePorId(1);
        Optional<String> nomeNaoEncontrado = buscarNomePorId(99);

        System.out.println("Nome encontrado: " + nomeEncontrado.orElse("Nao encontrado"));
        System.out.println("Nome nao encontrado: " + nomeNaoEncontrado.orElse("Nao encontrado"));
    }

    private static int dividir(int numero1, int numero2) {
        if (numero2 == 0) {
            throw new IllegalArgumentException("Nao e permitido dividir por zero.");
        }

        return numero1 / numero2;
    }

    private static Optional<String> buscarNomePorId(int id) {
        if (id == 1) {
            return Optional.of("Maria");
        }

        return Optional.empty();
    }

    static class Caixa<T> {
        private T conteudo;

        public void guardar(T conteudo) {
            this.conteudo = conteudo;
        }

        public T pegar() {
            return conteudo;
        }
    }
}
