package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dto.exemplar.ExemplarRequest;
import com.biblioteca.biblioteca.dto.exemplar.ExemplarResponse;
import com.biblioteca.biblioteca.entity.Exemplar;
import com.biblioteca.biblioteca.entity.Livro;
import com.biblioteca.biblioteca.enums.StatusExemplar;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.exception.RegraDeNegocioException;
import com.biblioteca.biblioteca.repository.ExemplarRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Service de Exemplar.
//
// Esta classe materializa a separacao entre:
// - Livro = obra bibliografica
// - Exemplar = unidade fisica do acervo
//
// A partir daqui, operacoes ligadas ao estoque fisico passam a fazer mais sentido,
// porque cada copia tem identidade e status proprios.
@Service
public class ExemplarService {

    private final ExemplarRepository exemplarRepository;
    private final LivroService livroService;

    public ExemplarService(ExemplarRepository exemplarRepository, LivroService livroService) {
        this.exemplarRepository = exemplarRepository;
        this.livroService = livroService;
    }

    @Transactional
    public ExemplarResponse cadastrar(ExemplarRequest request) {
        if (exemplarRepository.existsByCodigoPatrimonio(request.codigoPatrimonio())) {
            throw new RegraDeNegocioException("Ja existe um exemplar cadastrado com este codigo patrimonial.");
        }

        Livro livro = livroService.buscarEntidadePorId(request.livroId());

        Exemplar exemplar = new Exemplar();
        exemplar.setCodigoPatrimonio(request.codigoPatrimonio());
        exemplar.setLivro(livro);
        exemplar.setStatus(request.status());
        exemplar.setLocalizacao(request.localizacao());

        // Mantemos quantidadeDisponivel em Livro como campo derivado simplificado.
        // Em um passo futuro, poderiamos removê-lo por completo e calcular por consulta.
        if (request.status() == StatusExemplar.DISPONIVEL) {
            livro.setQuantidadeDisponivel(livro.getQuantidadeDisponivel() + 1);
        }

        return toResponse(exemplarRepository.save(exemplar));
    }

    @Transactional(readOnly = true)
    public List<ExemplarResponse> listarTodos() {
        return exemplarRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<ExemplarResponse> listarPorLivro(Long livroId) {
        return exemplarRepository.findByLivroId(livroId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ExemplarResponse buscarPorId(Long id) {
        return toResponse(buscarEntidadePorId(id));
    }

    @Transactional
    public ExemplarResponse atualizar(Long id, ExemplarRequest request) {
        Exemplar exemplar = buscarEntidadePorId(id);
        Livro novoLivro = livroService.buscarEntidadePorId(request.livroId());

        if (!exemplar.getCodigoPatrimonio().equals(request.codigoPatrimonio())
            && exemplarRepository.existsByCodigoPatrimonio(request.codigoPatrimonio())) {
            throw new RegraDeNegocioException("Ja existe outro exemplar com este codigo patrimonial.");
        }

        ajustarQuantidadeDisponivelAoMudarStatusOuLivro(exemplar, request.status(), novoLivro);

        exemplar.setCodigoPatrimonio(request.codigoPatrimonio());
        exemplar.setLivro(novoLivro);
        exemplar.setStatus(request.status());
        exemplar.setLocalizacao(request.localizacao());

        return toResponse(exemplarRepository.save(exemplar));
    }

    @Transactional
    public void remover(Long id) {
        Exemplar exemplar = buscarEntidadePorId(id);

        if (exemplar.getStatus() == StatusExemplar.EMPRESTADO) {
            throw new RegraDeNegocioException("Nao e permitido remover um exemplar emprestado.");
        }

        if (exemplar.getStatus() == StatusExemplar.DISPONIVEL) {
            Livro livro = exemplar.getLivro();
            livro.setQuantidadeDisponivel(Math.max(0, livro.getQuantidadeDisponivel() - 1));
        }

        exemplarRepository.delete(exemplar);
    }

    @Transactional(readOnly = true)
    public Exemplar buscarEntidadePorId(Long id) {
        return exemplarRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Exemplar nao encontrado com id: " + id));
    }

    @Transactional(readOnly = true)
    public Exemplar buscarExemplarDisponivelDoLivro(Long livroId) {
        return exemplarRepository.findFirstByLivroIdAndStatus(livroId, StatusExemplar.DISPONIVEL)
            .orElseThrow(() -> new RegraDeNegocioException("Nao existe exemplar disponivel para este livro."));
    }

    private void ajustarQuantidadeDisponivelAoMudarStatusOuLivro(
        Exemplar exemplar,
        StatusExemplar novoStatus,
        Livro novoLivro
    ) {
        StatusExemplar statusAnterior = exemplar.getStatus();
        Livro livroAnterior = exemplar.getLivro();

        // Se o exemplar estava disponivel e vai mudar de livro,
        // removemos a contagem do livro antigo e adicionamos no novo.
        if (statusAnterior == StatusExemplar.DISPONIVEL && !livroAnterior.getId().equals(novoLivro.getId())) {
            livroAnterior.setQuantidadeDisponivel(Math.max(0, livroAnterior.getQuantidadeDisponivel() - 1));
        }

        if (statusAnterior == StatusExemplar.DISPONIVEL && novoStatus == StatusExemplar.DISPONIVEL
            && !livroAnterior.getId().equals(novoLivro.getId())) {
            novoLivro.setQuantidadeDisponivel(novoLivro.getQuantidadeDisponivel() + 1);
        }

        if (statusAnterior != StatusExemplar.DISPONIVEL && novoStatus == StatusExemplar.DISPONIVEL) {
            novoLivro.setQuantidadeDisponivel(novoLivro.getQuantidadeDisponivel() + 1);
        }

        if (statusAnterior == StatusExemplar.DISPONIVEL && novoStatus != StatusExemplar.DISPONIVEL) {
            livroAnterior.setQuantidadeDisponivel(Math.max(0, livroAnterior.getQuantidadeDisponivel() - 1));
        }
    }

    private ExemplarResponse toResponse(Exemplar exemplar) {
        return new ExemplarResponse(
            exemplar.getId(),
            exemplar.getCodigoPatrimonio(),
            exemplar.getLivro().getId(),
            exemplar.getLivro().getTitulo(),
            exemplar.getStatus(),
            exemplar.getLocalizacao()
        );
    }
}
