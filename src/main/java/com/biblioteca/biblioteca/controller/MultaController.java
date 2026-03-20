package com.biblioteca.biblioteca.controller;

import com.biblioteca.biblioteca.dto.multa.MultaResponse;
import com.biblioteca.biblioteca.dto.multa.PagamentoMultaRequest;
import com.biblioteca.biblioteca.service.MultaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Controller de multa.
//
// Ele permite:
// - consultar multas
// - consultar multas por usuario
// - registrar pagamento
@RestController
@RequestMapping("/multas")
public class MultaController {

    private final MultaService multaService;

    public MultaController(MultaService multaService) {
        this.multaService = multaService;
    }

    @GetMapping
    public ResponseEntity<List<MultaResponse>> listar(@RequestParam(required = false) Long usuarioId) {
        if (usuarioId != null) {
            return ResponseEntity.ok(multaService.listarPorUsuario(usuarioId));
        }

        return ResponseEntity.ok(multaService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MultaResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(multaService.buscarPorId(id));
    }

    @PatchMapping("/{id}/pagar")
    public ResponseEntity<MultaResponse> pagar(
        @PathVariable Long id,
        @Valid @RequestBody PagamentoMultaRequest request
    ) {
        return ResponseEntity.ok(multaService.pagar(id, request));
    }
}
