package com.biblioteca.treinamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

public class SpringCore2 {

    public static void main(String[] args) {
        explicarIdeia();
        exemploComComponentScan();
    }

    private static void explicarIdeia() {
        System.out.println("=== Spring Core 2 ===");

        // Nesta classe, o foco nao e usar @Bean.
        // O foco e entender que o Spring tambem pode encontrar beans
        // automaticamente quando usamos anotacoes como @Service e @Repository.
        //
        // Esse processo se chama component scanning.
        // O Spring "varre" os pacotes configurados e registra como beans
        // as classes anotadas com estereotipos do framework.
        //
        // Depois disso, ele consegue injetar as dependencias.

        System.out.println("O Spring pode registrar beans sem @Bean.");
        System.out.println("Ele faz isso encontrando classes anotadas.");
        System.out.println();
    }

    private static void exemploComComponentScan() {
        System.out.println("=== Exemplo com component scanning ===");

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // Aqui pedimos ao Spring um bean pronto.
        // O service foi registrado porque esta anotado com @Service.
        BibliotecaService service = context.getBean(BibliotecaService.class);
        service.listarLivros();

        // Aqui pedimos outra classe que usa @Autowired em atributo.
        // Funciona, mas essa abordagem hoje e menos recomendada que construtor.
        BibliotecaFacade facade = context.getBean(BibliotecaFacade.class);
        facade.mostrarFonteDosDados();
        System.out.println();
    }

    @Configuration
    @ComponentScan(basePackageClasses = SpringCore2.class)
    static class AppConfig {
        // Nesta configuracao nao precisamos declarar @Bean para os exemplos abaixo.
        // O @ComponentScan manda o Spring procurar classes anotadas no mesmo pacote.
    }

    @Service
    static class BibliotecaService {
        private final BibliotecaRepository bibliotecaRepository;

        // Este e o modelo recomendado hoje.
        // Como a classe tem um unico construtor, o Spring injeta automaticamente,
        // mesmo sem escrever @Autowired aqui.
        public BibliotecaService(BibliotecaRepository bibliotecaRepository) {
            this.bibliotecaRepository = bibliotecaRepository;
        }

        public void listarLivros() {
            System.out.println("Service chamando o repository.");
            bibliotecaRepository.buscarTodos();
        }
    }

    @Repository
    static class BibliotecaRepository {
        public void buscarTodos() {
            System.out.println("Repository retornando livros do banco.");
        }
    }

    @Service
    static class BibliotecaFacade {

        // Isso tambem e injecao de dependencia.
        // Aqui o Spring injeta diretamente no atributo por causa do @Autowired.
        //
        // Funciona, mas nao e a forma preferida em codigo moderno.
        // A injecao por construtor e melhor para testes, clareza e imutabilidade.
        @Autowired
        private BibliotecaRepository bibliotecaRepository;

        public void mostrarFonteDosDados() {
            System.out.println("Facade recebeu o repository via @Autowired no atributo.");
            bibliotecaRepository.buscarTodos();
        }
    }
}
