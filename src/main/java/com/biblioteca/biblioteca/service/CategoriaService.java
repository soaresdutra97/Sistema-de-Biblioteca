package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dto.categoria.CategoriaRequest;
import com.biblioteca.biblioteca.dto.categoria.CategoriaResponse;
import com.biblioteca.biblioteca.entity.Categoria;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.exception.RegraDeNegocioException;
import com.biblioteca.biblioteca.repository.CategoriaRepository;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public CategoriaResponse cadastrar(CategoriaRequest request) {
        if (categoriaRepository.existsByNome(request.nome())) {
            throw new RegraDeNegocioException("Ja existe uma categoria cadastrada com este nome.");
        }

        Categoria categoria = new Categoria();
        categoria.setNome(request.nome());
        return toResponse(categoriaRepository.save(categoria));
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarTodos() {
        return categoriaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        return toResponse(buscarEntidadePorId(id));
    }

    @Transactional
    public CategoriaResponse atualizar(Long id, CategoriaRequest request) {
        Categoria categoria = buscarEntidadePorId(id);

        if (!categoria.getNome().equals(request.nome()) && categoriaRepository.existsByNome(request.nome())) {
            throw new RegraDeNegocioException("Ja existe outra categoria cadastrada com este nome.");
        }

        categoria.setNome(request.nome());
        return toResponse(categoriaRepository.save(categoria));
    }

    @Transactional
    public void remover(Long id) {
        categoriaRepository.delete(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Categoria buscarEntidadePorId(Long id) {
        return categoriaRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria nao encontrada com id: " + id));
    }

    @Transactional(readOnly = true)
    public Set<Categoria> buscarEntidadesPorIds(Set<Long> ids) {
        Set<Categoria> categorias = new LinkedHashSet<>();

        for (Long id : ids) {
            categorias.add(buscarEntidadePorId(id));
        }

        return categorias;
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNome());
    }
}
