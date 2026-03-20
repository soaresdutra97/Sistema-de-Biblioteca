package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dto.livro.LivroRequest;
import com.biblioteca.biblioteca.dto.livro.LivroResponse;
import com.biblioteca.biblioteca.service.LivroService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controller e a camada de entrada HTTP da aplicacao.
//
// Responsabilidade do controller:
// - receber requisicoes HTTP
// - ler dados da URL e do corpo da requisicao
// - delegar o trabalho para o service
// - devolver a resposta HTTP apropriada
//
// O controller NAO deve concentrar regra de negocio complexa.
// Ele deve ser fino, deixando a regra no service.
//
// @RestController marca esta classe como:
// - bean do Spring
// - controlador web REST
//
// O Spring encontra esse bean automaticamente no component scan.
@RestController

// @RequestMapping define o caminho base deste controller.
// Todos os endpoints daqui comecam com /livros.
@RequestMapping("/livros")
public class LivroController {

    private final LivroService livroService;

    // Injecao de dependencia por construtor, igual ao service.
    // O controller depende do service para executar a regra da aplicacao.
    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    // @PostMapping responde a requisicoes HTTP POST.
    // Em APIs REST, POST costuma ser usado para criar um novo recurso.
    //
    // @RequestBody diz ao Spring para ler o JSON enviado pelo cliente
    // e converter para LivroRequest.
    //
    // @Valid manda o Spring validar o DTO usando as anotacoes
    // como @NotBlank, @Size e @NotNull.
    @PostMapping
    public ResponseEntity<LivroResponse> cadastrar(@Valid @RequestBody LivroRequest request) {
        LivroResponse response = livroService.cadastrar(request);

        // ResponseEntity permite controlar status, headers e corpo da resposta.
        //
        // Aqui devolvemos 201 Created, que e o status mais comum para criacao.
        // Tambem informamos a URI do recurso criado.
        return ResponseEntity
            .created(URI.create("/livros/" + response.id()))
            .body(response);
    }

    // @GetMapping sem caminho adicional responde a GET /livros
    // e lista todos os registros.
    @GetMapping
    public ResponseEntity<List<LivroResponse>> listarTodos() {
        List<LivroResponse> response = livroService.listarTodos();
        return ResponseEntity.ok(response);
    }

    // @PathVariable captura um trecho da URL e injeta no parametro.
    //
    // Exemplo:
    // GET /livros/5
    // id = 5
    @GetMapping("/{id}")
    public ResponseEntity<LivroResponse> buscarPorId(@PathVariable Long id) {
        LivroResponse response = livroService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    // PUT costuma ser usado para atualizar um recurso inteiro.
    @PutMapping("/{id}")
    public ResponseEntity<LivroResponse> atualizar(@PathVariable Long id, @Valid @RequestBody LivroRequest request) {
        LivroResponse response = livroService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    // DELETE remove o recurso indicado.
    //
    // Aqui devolvemos 204 No Content, que e comum quando a remocao
    // foi bem-sucedida e nao precisamos mandar corpo na resposta.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        livroService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
