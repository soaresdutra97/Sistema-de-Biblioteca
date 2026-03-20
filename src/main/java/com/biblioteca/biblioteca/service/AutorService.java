package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dto.autor.AutorRequest;
import com.biblioteca.biblioteca.dto.autor.AutorResponse;
import com.biblioteca.biblioteca.entity.Autor;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.exception.RegraDeNegocioException;
import com.biblioteca.biblioteca.repository.AutorRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Service dedicado a Autor.
//
// Ao criar essa classe separada, mantemos a responsabilidade bem definida:
// regras de Autor ficam aqui, e nao espalhadas em LivroService.
@Service
public class AutorService {

    private final AutorRepository autorRepository;

    public AutorService(AutorRepository autorRepository) {
        this.autorRepository = autorRepository;
    }

    @Transactional
    public AutorResponse cadastrar(AutorRequest request) {
        if (autorRepository.existsByNome(request.nome())) {
            throw new RegraDeNegocioException("Ja existe um autor cadastrado com este nome.");
        }

        Autor autor = new Autor();
        autor.setNome(request.nome());

        return toResponse(autorRepository.save(autor));
    }

    @Transactional(readOnly = true)
    public List<AutorResponse> listarTodos() {
        return autorRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AutorResponse buscarPorId(Long id) {
        return toResponse(buscarEntidadePorId(id));
    }

    @Transactional
    public AutorResponse atualizar(Long id, AutorRequest request) {
        Autor autor = buscarEntidadePorId(id);

        if (!autor.getNome().equals(request.nome()) && autorRepository.existsByNome(request.nome())) {
            throw new RegraDeNegocioException("Ja existe outro autor cadastrado com este nome.");
        }

        autor.setNome(request.nome());
        return toResponse(autorRepository.save(autor));
    }

    @Transactional
    public void remover(Long id) {
        autorRepository.delete(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Autor buscarEntidadePorId(Long id) {
        return autorRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Autor nao encontrado com id: " + id));
    }

    private AutorResponse toResponse(Autor autor) {
        return new AutorResponse(autor.getId(), autor.getNome());
    }
}
