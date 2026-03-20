package com.biblioteca.treinamento;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class BoasPraticas {

    public static void main(String[] args) {
        exemploNomesClaroseObjetivos();
        exemploResponsabilidadeUnica();
        exemploEvitarMetodosGrandes();
        exemploEvitarValoresMagicos();
        exemploProgramarContraAbstracao();
    }

    private static void exemploNomesClaroseObjetivos() {
        System.out.println("=== Nomes claros e objetivos ===");

        // Ruim: nome generico que nao explica o que o metodo faz.
        int totalRuim = calc(3, 4);

        // Melhor: o nome deixa claro qual e a intencao.
        int totalBom = somarItensDoCarrinho(3, 4);

        System.out.println("Resultado ruim, mas funcional: " + totalRuim);
        System.out.println("Resultado com nome melhor: " + totalBom);
        System.out.println();
    }

    private static void exemploResponsabilidadeUnica() {
        System.out.println("=== Responsabilidade unica ===");

        // Uma classe nao deve fazer tudo ao mesmo tempo.
        // Aqui separamos o calculo do desconto da emissao do resumo.
        CalculadoraDeDesconto calculadora = new CalculadoraDeDesconto();
        EmissorDeResumo emissor = new EmissorDeResumo();

        BigDecimal valorOriginal = new BigDecimal("100.00");
        BigDecimal valorComDesconto = calculadora.aplicarDescontoDeDezPorCento(valorOriginal);

        emissor.exibirResumo(valorOriginal, valorComDesconto);
        System.out.println();
    }

    private static void exemploEvitarMetodosGrandes() {
        System.out.println("=== Evitar metodos grandes ===");

        // Em vez de colocar toda a regra em um metodo enorme,
        // dividimos em passos menores e com nomes que expliquem a intencao.
        Pedido pedido = new Pedido("Notebook", new BigDecimal("3500.00"), true);
        processarPedido(pedido);
        System.out.println();
    }

    private static void exemploEvitarValoresMagicos() {
        System.out.println("=== Evitar valores magicos ===");

        // Valor magico e um numero solto no codigo, sem contexto.
        // Constantes deixam a regra mais clara e mais facil de manter.
        BigDecimal salario = new BigDecimal("5000.00");
        BigDecimal bonus = salario.multiply(PERCENTUAL_BONUS_PADRAO);

        System.out.println("Bonus calculado com constante: " + bonus);
        System.out.println();
    }

    private static void exemploProgramarContraAbstracao() {
        System.out.println("=== Programar contra abstracao ===");

        // Boas praticas favorecem depender de interfaces ou contratos.
        // Isso facilita troca de implementacao e reduz acoplamento.
        List<String> nomes = new ArrayList<>();
        nomes.add("Ana");
        nomes.add("Carlos");

        imprimirNomes(nomes);
        System.out.println();
    }

    private static int calc(int a, int b) {
        return a + b;
    }

    private static int somarItensDoCarrinho(int quantidadeItem1, int quantidadeItem2) {
        return quantidadeItem1 + quantidadeItem2;
    }

    private static final BigDecimal PERCENTUAL_BONUS_PADRAO = new BigDecimal("0.10");

    private static void processarPedido(Pedido pedido) {
        validarPedido(pedido);
        BigDecimal total = calcularTotal(pedido);
        emitirMensagemFinal(pedido, total);
    }

    private static void validarPedido(Pedido pedido) {
        if (pedido.getNomeProduto() == null || pedido.getNomeProduto().isBlank()) {
            throw new IllegalArgumentException("O nome do produto e obrigatorio.");
        }

        if (pedido.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O preco deve ser maior que zero.");
        }
    }

    private static BigDecimal calcularTotal(Pedido pedido) {
        if (pedido.isFreteGratis()) {
            return pedido.getPreco();
        }

        return pedido.getPreco().add(new BigDecimal("25.00"));
    }

    private static void emitirMensagemFinal(Pedido pedido, BigDecimal total) {
        System.out.println("Pedido processado: " + pedido.getNomeProduto());
        System.out.println("Total final: R$ " + total);
    }

    private static void imprimirNomes(List<String> nomes) {
        for (String nome : nomes) {
            System.out.println("Nome: " + nome);
        }
    }

    static class CalculadoraDeDesconto {
        public BigDecimal aplicarDescontoDeDezPorCento(BigDecimal valor) {
            return valor.subtract(valor.multiply(new BigDecimal("0.10")));
        }
    }

    static class EmissorDeResumo {
        public void exibirResumo(BigDecimal valorOriginal, BigDecimal valorComDesconto) {
            System.out.println("Valor original: R$ " + valorOriginal);
            System.out.println("Valor com desconto: R$ " + valorComDesconto);
        }
    }

    static class Pedido {
        private final String nomeProduto;
        private final BigDecimal preco;
        private final boolean freteGratis;

        public Pedido(String nomeProduto, BigDecimal preco, boolean freteGratis) {
            this.nomeProduto = nomeProduto;
            this.preco = preco;
            this.freteGratis = freteGratis;
        }

        public String getNomeProduto() {
            return nomeProduto;
        }

        public BigDecimal getPreco() {
            return preco;
        }

        public boolean isFreteGratis() {
            return freteGratis;
        }
    }
}
