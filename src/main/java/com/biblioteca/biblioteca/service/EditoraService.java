package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dto.editora.EditoraRequest;
import com.biblioteca.biblioteca.dto.editora.EditoraResponse;
import com.biblioteca.biblioteca.entity.Editora;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.exception.RegraDeNegocioException;
import com.biblioteca.biblioteca.repository.EditoraRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EditoraService {

    private final EditoraRepository editoraRepository;

    public EditoraService(EditoraRepository editoraRepository) {
        this.editoraRepository = editoraRepository;
    }

    @Transactional
    public EditoraResponse cadastrar(EditoraRequest request) {
        if (editoraRepository.existsByNome(request.nome())) {
            throw new RegraDeNegocioException("Ja existe uma editora cadastrada com este nome.");
        }

        Editora editora = new Editora();
        editora.setNome(request.nome());

        return toResponse(editoraRepository.save(editora));
    }

    @Transactional(readOnly = true)
    public List<EditoraResponse> listarTodos() {
        return editoraRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public EditoraResponse buscarPorId(Long id) {
        return toResponse(buscarEntidadePorId(id));
    }

    @Transactional
    public EditoraResponse atualizar(Long id, EditoraRequest request) {
        Editora editora = buscarEntidadePorId(id);

        if (!editora.getNome().equals(request.nome()) && editoraRepository.existsByNome(request.nome())) {
            throw new RegraDeNegocioException("Ja existe outra editora cadastrada com este nome.");
        }

        editora.setNome(request.nome());
        return toResponse(editoraRepository.save(editora));
    }

    @Transactional
    public void remover(Long id) {
        editoraRepository.delete(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Editora buscarEntidadePorId(Long id) {
        return editoraRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Editora nao encontrada com id: " + id));
    }

    private EditoraResponse toResponse(Editora editora) {
        return new EditoraResponse(editora.getId(), editora.getNome());
    }
}
