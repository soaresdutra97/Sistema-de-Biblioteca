package com.biblioteca.treinamento;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class SpringCore {

    public static void main(String[] args) {
        explicarConceitosPrincipais();
        exemploSemSpring();
        exemploComSpring();
        explicarAnotacoesMaisComuns();
    }

    private static void explicarConceitosPrincipais() {
        System.out.println("=== Spring Core: conceitos principais ===");

        // Spring e um framework que ajuda a organizar a aplicacao e a conectar objetos.
        // O foco principal dele, neste inicio, e tirar de voce a responsabilidade
        // de criar e gerenciar manualmente todas as dependencias.
        //
        // Conceitos que voce precisa fixar agora:
        // 1. Bean: objeto gerenciado pelo Spring.
        // 2. IoC (Inversion of Control): o controle da criacao dos objetos sai da sua classe
        //    e passa para o container do Spring.
        // 3. DI (Dependency Injection): o Spring entrega as dependencias prontas para a classe.
        // 4. Container do Spring: estrutura interna que cria, guarda e conecta beans.

        System.out.println("Bean = objeto gerenciado pelo Spring.");
        System.out.println("IoC = Spring controla a criacao dos objetos.");
        System.out.println("DI = Spring injeta dependencias nas classes.");
        System.out.println();
    }

    private static void exemploSemSpring() {
        System.out.println("=== Exemplo sem Spring ===");

        // Aqui fazemos tudo manualmente.
        // O controller depende do service.
        // O service depende do repository.
        // Sem Spring, voce cria cada objeto na mao e faz o encaixe manual.
        LivroRepository repository = new LivroRepository();
        LivroService service = new LivroService(repository);
        LivroController controller = new LivroController(service);

        controller.listarLivros();
        System.out.println();
    }

    private static void exemploComSpring() {
        System.out.println("=== Exemplo com Spring ===");

        // Aqui criamos um pequeno contexto do Spring.
        // Pense nesse contexto como uma "fabrica inteligente" de objetos.
        //
        // O Spring olha para a classe de configuracao abaixo, encontra os metodos @Bean
        // e cria os objetos para nos.
        //
        // Repare neste ponto importante:
        // o metodo livroService recebe LivroRepository como parametro.
        // Isso e injecao de dependencia.
        // O Spring percebe a dependencia, cria o repository e o entrega ao service.
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        LivroController controller = context.getBean(LivroController.class);
        controller.listarLivros();
        System.out.println();
    }

    private static void explicarAnotacoesMaisComuns() {
        System.out.println("=== Anotacoes que voce vai ver no Spring ===");

        // @Configuration
        // Marca uma classe de configuracao do Spring.
        //
        // @Bean
        // Diz ao Spring: "crie e gerencie o objeto retornado por este metodo".
        //
        // Mais adiante, em aplicacoes Spring Boot, voce vai ver muito:
        // @Component: bean generico.
        // @Service: bean da camada de regra de negocio.
        // @Repository: bean da camada de acesso a dados.
        // @Controller / @RestController: bean que recebe requisicoes HTTP.

        System.out.println("@Configuration = classe de configuracao.");
        System.out.println("@Bean = objeto gerenciado pelo Spring.");
        System.out.println("@Service = regra de negocio.");
        System.out.println("@Repository = acesso a dados.");
        System.out.println("@RestController = entrada HTTP da aplicacao.");
        System.out.println();
    }

    static class LivroController {
        private final LivroService livroService;

        // Injecao por construtor:
        // o controller nao cria o service.
        // Ele apenas declara que precisa dele.
        public LivroController(LivroService livroService) {
            this.livroService = livroService;
        }

        public void listarLivros() {
            System.out.println("Controller recebeu a chamada.");
            livroService.listarLivros();
        }
    }

    static class LivroService {
        private final LivroRepository livroRepository;

        public LivroService(LivroRepository livroRepository) {
            this.livroRepository = livroRepository;
        }

        public void listarLivros() {
            System.out.println("Service aplicando regra de negocio.");
            livroRepository.buscarTodos();
        }
    }

    static class LivroRepository {
        public void buscarTodos() {
            System.out.println("Repository buscando livros no banco.");
        }
    }

    @Configuration
    static class AppConfig {

        // Cada metodo @Bean devolve um objeto que o Spring passa a gerenciar.
        // A partir daqui, esses objetos viram beans.

        @Bean
        public LivroRepository livroRepository() {
            return new LivroRepository();
        }

        @Bean
        public LivroService livroService(LivroRepository livroRepository) {
            return new LivroService(livroRepository);
        }

        @Bean
        public LivroController livroController(LivroService livroService) {
            return new LivroController(livroService);
        }
    }
}
