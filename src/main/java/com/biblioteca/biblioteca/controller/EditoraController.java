package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dto.editora.EditoraRequest;
import com.biblioteca.biblioteca.dto.editora.EditoraResponse;
import com.biblioteca.biblioteca.service.EditoraService;
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

@RestController
@RequestMapping("/editoras")
public class EditoraController {

    private final EditoraService editoraService;

    public EditoraController(EditoraService editoraService) {
        this.editoraService = editoraService;
    }

    @PostMapping
    public ResponseEntity<EditoraResponse> cadastrar(@Valid @RequestBody EditoraRequest request) {
        EditoraResponse response = editoraService.cadastrar(request);
        return ResponseEntity.created(URI.create("/editoras/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EditoraResponse>> listarTodos() {
        return ResponseEntity.ok(editoraService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditoraResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(editoraService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EditoraResponse> atualizar(@PathVariable Long id, @Valid @RequestBody EditoraRequest request) {
        return ResponseEntity.ok(editoraService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        editoraService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
