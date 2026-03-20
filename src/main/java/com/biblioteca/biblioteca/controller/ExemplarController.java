package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dto.exemplar.ExemplarRequest;
import com.biblioteca.biblioteca.dto.exemplar.ExemplarResponse;
import com.biblioteca.biblioteca.service.ExemplarService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Controller de Exemplar.
//
// Este recurso deixa explicita a diferenca entre o cadastro bibliografico
// do livro e o controle operacional do acervo fisico.
@RestController
@RequestMapping("/exemplares")
public class ExemplarController {

    private final ExemplarService exemplarService;

    public ExemplarController(ExemplarService exemplarService) {
        this.exemplarService = exemplarService;
    }

    @PostMapping
    public ResponseEntity<ExemplarResponse> cadastrar(@Valid @RequestBody ExemplarRequest request) {
        ExemplarResponse response = exemplarService.cadastrar(request);
        return ResponseEntity.created(URI.create("/exemplares/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ExemplarResponse>> listarTodos(@RequestParam(required = false) Long livroId) {
        if (livroId != null) {
            return ResponseEntity.ok(exemplarService.listarPorLivro(livroId));
        }

        return ResponseEntity.ok(exemplarService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExemplarResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(exemplarService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExemplarResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ExemplarRequest request) {
        return ResponseEntity.ok(exemplarService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        exemplarService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
