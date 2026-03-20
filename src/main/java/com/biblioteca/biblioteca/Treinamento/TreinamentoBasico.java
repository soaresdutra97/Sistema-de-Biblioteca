package com.biblioteca.treinamento;

public class TreinamentoBasico {

    public static void main(String[] args) {
        exemploVariaveis();
        exemploOperadoresEIf();
        exemploSwitch();
        exemploFor();
        exemploWhile();
        exemploMetodos();
    }

    private static void exemploVariaveis() {
        String nome = "Carlos";
        int idade = 20;
        double altura = 1.75;
        char inicial = 'C';
        boolean matriculado = true;

        System.out.println("=== Variaveis ===");
        System.out.println("Nome: " + nome);
        System.out.println("Idade: " + idade);
        System.out.println("Altura: " + altura);
        System.out.println("Inicial: " + inicial);
        System.out.println("Matriculado: " + matriculado);
        System.out.println();
    }

    private static void exemploOperadoresEIf() {
        int idade = 19;
        boolean temCarteira = true;
        int numero1 = 10;
        int numero2 = 5;

        int soma = numero1 + numero2;
        int subtracao = numero1 - numero2;
        int multiplicacao = numero1 * numero2;
        int divisao = numero1 / numero2;

        System.out.println("=== Operadores e if ===");
        System.out.println("Soma: " + soma);
        System.out.println("Subtracao: " + subtracao);
        System.out.println("Multiplicacao: " + multiplicacao);
        System.out.println("Divisao: " + divisao);

        if (idade >= 18 && temCarteira) {
            System.out.println("Pode dirigir");
        } else {
            System.out.println("Nao pode dirigir");
        }

        System.out.println();
    }

    private static void exemploSwitch() {
        int diaDaSemana = 3;

        System.out.println("=== Switch ===");

        switch (diaDaSemana) {
            case 1:
                System.out.println("Domingo");
                break;
            case 2:
                System.out.println("Segunda-feira");
                break;
            case 3:
                System.out.println("Terca-feira");
                break;
            case 4:
                System.out.println("Quarta-feira");
                break;
            case 5:
                System.out.println("Quinta-feira");
                break;
            case 6:
                System.out.println("Sexta-feira");
                break;
            case 7:
                System.out.println("Sabado");
                break;
            default:
                System.out.println("Dia invalido");
        }

        System.out.println();
    }

    private static void exemploFor() {
        System.out.println("=== For ===");

        for (int i = 1; i <= 5; i++) {
            System.out.println("Contagem: " + i);
        }

        System.out.println();
    }

    private static void exemploWhile() {
        System.out.println("=== While ===");

        int contador = 1;

        while (contador <= 3) {
            System.out.println("Valor do contador: " + contador);
            contador++;
        }

        System.out.println();
    }

    private static void exemploMetodos() {
        System.out.println("=== Metodos ===");

        int resultadoSoma = somar(8, 4);
        String mensagem = gerarMensagem("Ana");

        System.out.println("Resultado da soma: " + resultadoSoma);
        System.out.println(mensagem);
    }

    private static int somar(int numero1, int numero2) {
        return numero1 + numero2;
    }

    private static String gerarMensagem(String nome) {
        return "Bem-vinda, " + nome + "!";
    }
}
