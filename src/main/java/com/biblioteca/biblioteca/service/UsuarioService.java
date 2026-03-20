package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dto.usuario.UsuarioRequest;
import com.biblioteca.biblioteca.dto.usuario.UsuarioResponse;
import com.biblioteca.biblioteca.entity.Usuario;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.exception.RegraDeNegocioException;
import com.biblioteca.biblioteca.repository.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Service de Usuario.
//
// Aqui ficam as regras de negocio relacionadas ao usuario.
// Exemplos:
// - nao permitir email duplicado
// - nao permitir documento duplicado
// - buscar usuario por id e falhar de forma clara se nao existir
//
// Observacao importante:
// as validacoes de FORMATO estao no DTO, por exemplo:
// - email valido
// - documento com 11 digitos
//
// Ja as validacoes de NEGOCIO ficam aqui no service.
@Service
public class UsuarioService {

    // Esta referencia aponta para o repository que acessa o banco.
    //
    // "private":
    // significa que somente esta classe acessa diretamente esse atributo.
    //
    // "final":
    // significa que a referencia deve ser atribuida uma unica vez,
    // normalmente no construtor.
    //
    // Isso combina muito bem com injecao por construtor, porque:
    // - a dependencia fica obrigatoria
    // - a classe nasce completa
    // - evitamos reatribuir a dependencia por engano
    //
    // Em codigo orientado a boas praticas, essa combinacao
    // (private + final + construtor) e muito comum.
    private final UsuarioRepository usuarioRepository;

    // O Spring chama este construtor quando cria o bean UsuarioService.
    //
    // Como existe um unico construtor, o Spring entende automaticamente
    // que precisa injetar um bean do tipo UsuarioRepository aqui.
    //
    // Ou seja:
    // o UsuarioService depende de UsuarioRepository,
    // e o Spring entrega essa dependencia pronta.
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // @Transactional abre uma transacao para esta operacao.
    //
    // De forma simples, uma transacao agrupa operacoes que precisam acontecer
    // com consistencia no banco.
    //
    // Se algo der errado no meio do processo, o Spring/Hibernate pode desfazer
    // as alteracoes da transacao, evitando estados quebrados.
    //
    // Como este metodo cadastra dados, faz sentido usar a versao padrao
    // de @Transactional, permitindo escrita.
    @Transactional
    public UsuarioResponse cadastrar(UsuarioRequest request) {
        // Aqui aplicamos regra de negocio.
        // O DTO ja validou formato do email e documento.
        // Agora o service valida regras do sistema, como duplicidade.
        validarDuplicidade(request.email(), request.documento(), null);

        // Criamos a entity a partir do DTO recebido.
        // Esse processo de transformar request em entity
        // tambem e comum na camada service.
        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setDocumento(request.documento());
        usuario.setAtivo(request.ativo());

        // save(...) persiste a entity no banco.
        // Como estamos usando Spring Data JPA,
        // o repository ja tem esse metodo pronto.
        Usuario usuarioSalvo = usuarioRepository.save(usuario);
        return toResponse(usuarioSalvo);
    }

    // readOnly = true informa que esta transacao e somente de leitura.
    //
    // Isso deixa a intencao mais clara e, em alguns cenarios,
    // pode permitir otimizacoes.
    //
    // Sempre que o metodo apenas consulta dados, essa opcao faz sentido.
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorId(Long id) {
        // Aqui reaproveitamos um metodo interno que busca a entity
        // e lanca exception se nao encontrar.
        Usuario usuario = buscarEntidadePorId(id);
        return toResponse(usuario);
    }

    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        // Primeiro garantimos que o usuario existe.
        Usuario usuario = buscarEntidadePorId(id);

        // Depois validamos duplicidade, mas agora considerando
        // que estamos editando um registro existente.
        validarDuplicidade(request.email(), request.documento(), usuario);

        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setDocumento(request.documento());
        usuario.setAtivo(request.ativo());

        Usuario usuarioAtualizado = usuarioRepository.save(usuario);
        return toResponse(usuarioAtualizado);
    }

    @Transactional
    public void remover(Long id) {
        Usuario usuario = buscarEntidadePorId(id);
        usuarioRepository.delete(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarEntidadePorId(Long id) {
        // findById(...) devolve Optional<Usuario>.
        //
        // Optional representa "pode existir ou nao".
        // Se o usuario estiver presente, usamos o valor.
        // Se nao estiver, lancamos uma exception com orElseThrow(...).
        //
        // Esse padrao e muito comum em services Spring.
        return usuarioRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado com id: " + id));
    }

    private void validarDuplicidade(String email, String documento, Usuario usuarioExistente) {
        // Estes booleanos ajudam a decidir se realmente precisamos consultar
        // duplicidade no banco ao atualizar.
        //
        // Exemplo:
        // se o email nao mudou, nao faz sentido acusar o proprio usuario
        // como duplicado.
        boolean emailMudou = usuarioExistente == null || !usuarioExistente.getEmail().equals(email);
        boolean documentoMudou = usuarioExistente == null || !usuarioExistente.getDocumento().equals(documento);

        if (emailMudou && usuarioRepository.existsByEmail(email)) {
            throw new RegraDeNegocioException("Ja existe um usuario cadastrado com este email.");
        }

        if (documentoMudou && usuarioRepository.existsByDocumento(documento)) {
            throw new RegraDeNegocioException("Ja existe um usuario cadastrado com este documento.");
        }
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        // Este metodo centraliza a conversao de entity para DTO de saida.
        //
        // Isso evita repetir o mesmo bloco em varios metodos
        // e deixa o codigo mais organizado.
        return new UsuarioResponse(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getDocumento(),
            usuario.getAtivo()
        );
    }
}
