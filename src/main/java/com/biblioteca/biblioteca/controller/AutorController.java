package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dto.autor.AutorRequest;
import com.biblioteca.biblioteca.dto.autor.AutorResponse;
import com.biblioteca.biblioteca.service.AutorService;
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

// Este controller permite gerenciar autores como recurso proprio da API.
//
// Essa e uma consequencia natural de evoluir o dominio:
// se Autor virou entidade de negocio, ele precisa de uma porta de entrada propria.
@RestController
@RequestMapping("/autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @PostMapping
    public ResponseEntity<AutorResponse> cadastrar(@Valid @RequestBody AutorRequest request) {
        AutorResponse response = autorService.cadastrar(request);
        return ResponseEntity.created(URI.create("/autores/" + response.id())).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AutorResponse>> listarTodos() {
        return ResponseEntity.ok(autorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(autorService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AutorResponse> atualizar(@PathVariable Long id, @Valid @RequestBody AutorRequest request) {
        return ResponseEntity.ok(autorService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        autorService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
