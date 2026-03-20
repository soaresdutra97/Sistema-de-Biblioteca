package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dto.livro.LivroRequest;
import com.biblioteca.biblioteca.dto.livro.LivroResponse;
import com.biblioteca.biblioteca.entity.Categoria;
import com.biblioteca.biblioteca.entity.Livro;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.exception.RegraDeNegocioException;
import com.biblioteca.biblioteca.repository.LivroRepository;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Esta e a camada de service de Livro.
//
// Responsabilidade do service:
// - aplicar regra de negocio
// - orquestrar o uso do repository
// - converter entity para DTO e vice-versa, quando fizer sentido
//
// O service NAO deveria conhecer detalhes de HTTP.
// Por isso ele nao lida com ResponseEntity, endpoint ou anotacoes de rota.
//
// @Service marca esta classe como bean do Spring.
// Isso significa que o Spring vai:
// - criar um objeto dessa classe
// - gerencia-lo no container
// - permitir que outras classes o recebam por injecao de dependencia
@Service
public class LivroService {

    // Esta e a dependencia principal desta classe.
    //
    // O service depende do repository para acessar o banco.
    // A palavra "final" reforca que essa dependencia e fixa
    // depois da construcao do objeto.
    private final LivroRepository livroRepository;
    private final AutorService autorService;
    private final EditoraService editoraService;
    private final CategoriaService categoriaService;

    // Aqui acontece injecao de dependencia por construtor.
    //
    // A classe NAO faz:
    // new LivroRepository()
    //
    // Em vez disso, ela declara do que precisa.
    // O Spring olha para o construtor, encontra o bean LivroRepository
    // e injeta a dependencia automaticamente.
    //
    // Isso e um exemplo claro de Inversao de Controle:
    // a criacao e montagem dos objetos nao esta nas suas maos,
    // e sim nas maos do container do Spring.
    public LivroService(
        LivroRepository livroRepository,
        AutorService autorService,
        EditoraService editoraService,
        CategoriaService categoriaService
    ) {
        this.livroRepository = livroRepository;
        this.autorService = autorService;
        this.editoraService = editoraService;
        this.categoriaService = categoriaService;
    }

    @Transactional
    public LivroResponse cadastrar(LivroRequest request) {
        // Regra de negocio:
        // ISBN nao pode se repetir no sistema.
        if (livroRepository.existsByIsbn(request.isbn())) {
            throw new RegraDeNegocioException("Ja existe um livro cadastrado com este ISBN.");
        }

        // Convertemos o DTO recebido em entity.
        Livro livro = new Livro();
        livro.setTitulo(request.titulo());
        livro.setAutor(autorService.buscarEntidadePorId(request.autorId()));
        livro.setEditora(editoraService.buscarEntidadePorId(request.editoraId()));
        livro.setCategorias(categoriaService.buscarEntidadesPorIds(request.categoriaIds()));
        livro.setIsbn(request.isbn());
        livro.setAnoPublicacao(request.anoPublicacao());
        livro.setQuantidadeDisponivel(request.quantidadeDisponivel());

        // O repository salva a entity e devolve o objeto persistido.
        // Normalmente o id ja vem preenchido depois dessa operacao.
        Livro livroSalvo = livroRepository.save(livro);
        return toResponse(livroSalvo);
    }

    @Transactional(readOnly = true)
    public List<LivroResponse> listarTodos() {
        // findAll() traz todas as entidades.
        // Depois usamos stream para converter cada Livro em LivroResponse.
        return livroRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public LivroResponse buscarPorId(Long id) {
        Livro livro = buscarEntidadePorId(id);
        return toResponse(livro);
    }

    @Transactional
    public LivroResponse atualizar(Long id, LivroRequest request) {
        Livro livro = buscarEntidadePorId(id);

        // Quando estamos atualizando, o ISBN so pode ser validado contra duplicidade
        // se ele realmente mudou.
        if (!livro.getIsbn().equals(request.isbn()) && livroRepository.existsByIsbn(request.isbn())) {
            throw new RegraDeNegocioException("Ja existe outro livro cadastrado com este ISBN.");
        }

        livro.setTitulo(request.titulo());
        livro.setAutor(autorService.buscarEntidadePorId(request.autorId()));
        livro.setEditora(editoraService.buscarEntidadePorId(request.editoraId()));
        livro.setCategorias(categoriaService.buscarEntidadesPorIds(request.categoriaIds()));
        livro.setIsbn(request.isbn());
        livro.setAnoPublicacao(request.anoPublicacao());
        livro.setQuantidadeDisponivel(request.quantidadeDisponivel());

        Livro livroAtualizado = livroRepository.save(livro);
        return toResponse(livroAtualizado);
    }

    @Transactional
    public void remover(Long id) {
        // Antes de remover, buscamos a entity.
        // Isso garante duas coisas:
        // 1. validamos se o recurso existe
        // 2. evitamos apagar "no escuro"
        Livro livro = buscarEntidadePorId(id);
        livroRepository.delete(livro);
    }

    // Este metodo e util para reaproveitar a busca da entity dentro do service.
    // Repare que aqui retornamos a entidade, nao o DTO.
    // Isso e util para regras internas da aplicacao.
    @Transactional(readOnly = true)
    public Livro buscarEntidadePorId(Long id) {
        return livroRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Livro nao encontrado com id: " + id));
    }

    private LivroResponse toResponse(Livro livro) {
        // Conversao centralizada de entity para DTO.
        Set<String> categorias = livro.getCategorias()
            .stream()
            .map(Categoria::getNome)
            .collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));

        return new LivroResponse(
            livro.getId(),
            livro.getTitulo(),
            livro.getAutor().getId(),
            livro.getAutor().getNome(),
            livro.getEditora().getId(),
            livro.getEditora().getNome(),
            categorias,
            livro.getIsbn(),
            livro.getAnoPublicacao(),
            livro.getQuantidadeDisponivel()
        );
    }
}
