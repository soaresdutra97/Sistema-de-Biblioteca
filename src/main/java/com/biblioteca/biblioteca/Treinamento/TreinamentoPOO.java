package com.biblioteca.treinamento;

public class TreinamentoPOO {

    public static void main(String[] args) {
        exemploEncapsulamento();
        exemploHeranca();
        exemploPolimorfismo();
        exemploAbstracao();
        exemploComposicao();
    }

    private static void exemploEncapsulamento() {
        System.out.println("=== Encapsulamento ===");

        Livro livro = new Livro("Java Basico", "Maria", 2024);
        livro.setAnoPublicacao(2025);

        System.out.println("Titulo: " + livro.getTitulo());
        System.out.println("Autor: " + livro.getAutor());
        System.out.println("Ano: " + livro.getAnoPublicacao());
        System.out.println();
    }

    private static void exemploHeranca() {
        System.out.println("=== Heranca ===");

        Carro carro = new Carro("Toyota", "Corolla", 2020);
        CarroEletrico carroEletrico = new CarroEletrico("Tesla", "Model 3", 2023, 450);

        carro.exibirInformacoes();
        carroEletrico.exibirInformacoes();
        System.out.println();
    }

    private static void exemploPolimorfismo() {
        System.out.println("=== Polimorfismo ===");

        Animal cachorro = new Cachorro();
        Animal gato = new Gato();

        cachorro.emitirSom();
        gato.emitirSom();
        System.out.println();
    }

    private static void exemploAbstracao() {
        System.out.println("=== Abstracao com interface ===");

        Pagamento pagamentoPix = new PagamentoPix();
        Pagamento pagamentoCartao = new PagamentoCartao();

        pagamentoPix.pagar(150.0);
        pagamentoCartao.pagar(320.0);
        System.out.println();
    }

    private static void exemploComposicao() {
        System.out.println("=== Composicao ===");

        Autor autor = new Autor("Robert Martin");
        LivroComAutor livro = new LivroComAutor("Clean Code", autor);

        livro.exibirDetalhes();
    }

    static class Livro {
        private String titulo;
        private String autor;
        private int anoPublicacao;

        public Livro(String titulo, String autor, int anoPublicacao) {
            this.titulo = titulo;
            this.autor = autor;
            this.anoPublicacao = anoPublicacao;
        }

        public String getTitulo() {
            return titulo;
        }

        public String getAutor() {
            return autor;
        }

        public int getAnoPublicacao() {
            return anoPublicacao;
        }

        public void setAnoPublicacao(int anoPublicacao) {
            if (anoPublicacao > 0) {
                this.anoPublicacao = anoPublicacao;
            }
        }
    }

    static class Carro {
        protected String marca;
        protected String modelo;
        protected int ano;

        public Carro(String marca, String modelo, int ano) {
            this.marca = marca;
            this.modelo = modelo;
            this.ano = ano;
        }

        public void exibirInformacoes() {
            System.out.println("Carro: " + marca + " " + modelo + " - " + ano);
        }
    }

    static class CarroEletrico extends Carro {
        private int autonomiaBateria;

        public CarroEletrico(String marca, String modelo, int ano, int autonomiaBateria) {
            super(marca, modelo, ano);
            this.autonomiaBateria = autonomiaBateria;
        }

        @Override
        public void exibirInformacoes() {
            System.out.println(
                "Carro eletrico: " + marca + " " + modelo + " - " + ano + " - autonomia: " + autonomiaBateria + " km"
            );
        }
    }

    static class Animal {
        public void emitirSom() {
            System.out.println("Som generico");
        }
    }

    static class Cachorro extends Animal {
        @Override
        public void emitirSom() {
            System.out.println("Au au");
        }
    }

    static class Gato extends Animal {
        @Override
        public void emitirSom() {
            System.out.println("Miau");
        }
    }

    interface Pagamento {
        void pagar(double valor);
    }

    static class PagamentoPix implements Pagamento {
        @Override
        public void pagar(double valor) {
            System.out.println("Pagamento via Pix: R$ " + valor);
        }
    }

    static class PagamentoCartao implements Pagamento {
        @Override
        public void pagar(double valor) {
            System.out.println("Pagamento via cartao: R$ " + valor);
        }
    }

    static class Autor {
        private String nome;

        public Autor(String nome) {
            this.nome = nome;
        }

        public String getNome() {
            return nome;
        }
    }

    static class LivroComAutor {
        private String titulo;
        private Autor autor;

        public LivroComAutor(String titulo, Autor autor) {
            this.titulo = titulo;
            this.autor = autor;
        }

        public void exibirDetalhes() {
            System.out.println("Livro: " + titulo + " - Autor: " + autor.getNome());
        }
    }
}
