package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dto.emprestimo.DevolucaoRequest;
import com.biblioteca.biblioteca.dto.emprestimo.EmprestimoRequest;
import com.biblioteca.biblioteca.dto.emprestimo.EmprestimoResponse;
import com.biblioteca.biblioteca.service.EmprestimoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Controller de emprestimos.
//
// Aqui aparecem dois tipos de operacao:
// - criar emprestimo
// - devolver livro
//
// Isso mostra que controller nao serve apenas para CRUD puro.
// Ele tambem expoe casos de uso especificos do dominio.
@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController(EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    public ResponseEntity<EmprestimoResponse> registrarEmprestimo(@Valid @RequestBody EmprestimoRequest request) {
        EmprestimoResponse response = emprestimoService.registrarEmprestimo(request);

        return ResponseEntity
            .created(URI.create("/emprestimos/" + response.id()))
            .body(response);
    }

    // Este endpoint agora aceita filtro opcional por usuario.
    //
    // Exemplos:
    // GET /emprestimos
    // GET /emprestimos?usuarioId=1
    //
    // Isso mantem a API simples:
    // um unico endpoint de listagem, com ou sem filtro.
    @GetMapping
    public ResponseEntity<List<EmprestimoResponse>> listarTodos(@RequestParam(required = false) Long usuarioId) {
        List<EmprestimoResponse> response;

        if (usuarioId != null) {
            response = emprestimoService.listarPorUsuario(usuarioId);
        } else {
            response = emprestimoService.listarTodos();
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmprestimoResponse> buscarPorId(@PathVariable Long id) {
        EmprestimoResponse response = emprestimoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    // PATCH e frequentemente usado quando queremos alterar parte do recurso
    // ou executar uma acao especifica sobre ele.
    //
    // Aqui a rota deixa isso bem explicito:
    // PATCH /emprestimos/{id}/devolucao
    //
    // Essa clareza de rota ajuda muito na legibilidade da API.
    @PatchMapping("/{id}/devolucao")
    public ResponseEntity<EmprestimoResponse> devolverLivro(
        @PathVariable Long id,
        @Valid @RequestBody DevolucaoRequest request
    ) {
        EmprestimoResponse response = emprestimoService.devolverLivro(id, request);
        return ResponseEntity.ok(response);
    }
}
