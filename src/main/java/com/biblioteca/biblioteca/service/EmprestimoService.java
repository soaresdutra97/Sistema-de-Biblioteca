package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dto.emprestimo.DevolucaoRequest;
import com.biblioteca.biblioteca.dto.emprestimo.EmprestimoRequest;
import com.biblioteca.biblioteca.dto.emprestimo.EmprestimoResponse;
import com.biblioteca.biblioteca.entity.Emprestimo;
import com.biblioteca.biblioteca.entity.Exemplar;
import com.biblioteca.biblioteca.entity.Usuario;
import com.biblioteca.biblioteca.enums.StatusExemplar;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.exception.RegraDeNegocioException;
import com.biblioteca.biblioteca.repository.EmprestimoRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Este service concentra as regras mais "dinamicas" do sistema.
//
// Livro e Usuario possuem CRUD mais direto.
// Emprestimo, por outro lado, envolve operacoes de negocio:
// - registrar emprestimo
// - verificar disponibilidade de exemplar
// - verificar situacao do usuario
// - devolver livro
// - atualizar status do exemplar
//
// Por isso esta classe e um bom exemplo de onde a camada service agrega valor.
@Service
public class EmprestimoService {

    // Repository da entidade principal deste service.
    private final EmprestimoRepository emprestimoRepository;

    // Aqui injetamos outros services em vez de acessar diretamente
    // os repositories deles.
    //
    // Isso ajuda a centralizar regras relacionadas a Livro, Exemplar e Usuario
    // nos seus proprios services.
    private final ExemplarService exemplarService;
    private final UsuarioService usuarioService;
    private final MultaService multaService;

    // Repare que este service depende de:
    // - um repository
    // - outros services
    //
    // Isso pode acontecer quando a regra de negocio de uma operacao
    // precisa conversar com mais de um agregado do sistema.
    //
    // O Spring injeta todas essas dependencias porque todas elas sao beans.
    public EmprestimoService(
        EmprestimoRepository emprestimoRepository,
        ExemplarService exemplarService,
        UsuarioService usuarioService,
        MultaService multaService
    ) {
        this.emprestimoRepository = emprestimoRepository;
        this.exemplarService = exemplarService;
        this.usuarioService = usuarioService;
        this.multaService = multaService;
    }

    @Transactional
    public EmprestimoResponse registrarEmprestimo(EmprestimoRequest request) {
        // Primeiro buscamos um exemplar disponivel do livro solicitado.
        // Essa e a grande mudanca do dominio:
        // nao emprestamos mais "o livro" genericamente,
        // e sim uma copia fisica especifica.
        Exemplar exemplar = exemplarService.buscarExemplarDisponivelDoLivro(request.livroId());
        Usuario usuario = usuarioService.buscarEntidadePorId(request.usuarioId());

        // Regras de negocio do emprestimo:
        if (Boolean.FALSE.equals(usuario.getAtivo())) {
            throw new RegraDeNegocioException("Usuario inativo nao pode realizar emprestimos.");
        }

        if (multaService.usuarioPossuiMultaPendente(usuario.getId())) {
            throw new RegraDeNegocioException("Usuario inadimplente nao pode realizar novos emprestimos.");
        }

        if (emprestimoRepository.existsByExemplarIdAndAtivoTrue(exemplar.getId())) {
            throw new RegraDeNegocioException("Este exemplar ja possui um emprestimo ativo.");
        }

        // Criamos o registro de emprestimo.
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setExemplar(exemplar);
        emprestimo.setUsuario(usuario);
        emprestimo.setDataEmprestimo(LocalDate.now());
        emprestimo.setDataPrevistaDevolucao(request.dataPrevistaDevolucao());
        emprestimo.setDataDevolucaoReal(null);
        emprestimo.setAtivo(true);

        // Ao emprestar, marcamos explicitamente o exemplar como EMPRESTADO.
        exemplar.setStatus(StatusExemplar.EMPRESTADO);
        exemplar.getLivro().setQuantidadeDisponivel(
            Math.max(0, exemplar.getLivro().getQuantidadeDisponivel() - 1)
        );

        Emprestimo emprestimoSalvo = emprestimoRepository.save(emprestimo);
        return toResponse(emprestimoSalvo);
    }

    @Transactional(readOnly = true)
    public List<EmprestimoResponse> listarTodos() {
        return emprestimoRepository.findAll()
            .stream()
            .map(this::toResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public EmprestimoResponse buscarPorId(Long id) {
        Emprestimo emprestimo = buscarEntidadePorId(id);
        return toResponse(emprestimo);
    }

    @Transactional
    public EmprestimoResponse devolverLivro(Long emprestimoId, DevolucaoRequest request) {
        Emprestimo emprestimo = buscarEntidadePorId(emprestimoId);

        // Nao faz sentido devolver novamente algo que ja foi devolvido.
        if (Boolean.FALSE.equals(emprestimo.getAtivo())) {
            throw new RegraDeNegocioException("Este emprestimo ja foi finalizado.");
        }

        // Marcamos a devolucao e encerramos o emprestimo.
        emprestimo.setDataDevolucaoReal(request.dataDevolucaoReal());
        emprestimo.setAtivo(false);

        // Ao devolver, o exemplar volta a ficar disponivel.
        Exemplar exemplar = emprestimo.getExemplar();
        exemplar.setStatus(StatusExemplar.DISPONIVEL);
        exemplar.getLivro().setQuantidadeDisponivel(exemplar.getLivro().getQuantidadeDisponivel() + 1);

        Emprestimo emprestimoAtualizado = emprestimoRepository.save(emprestimo);

        // Depois da devolucao, verificamos se houve atraso suficiente
        // para gerar multa.
        multaService.gerarSeHouverAtraso(emprestimoAtualizado);

        return toResponse(emprestimoAtualizado);
    }

    @Transactional(readOnly = true)
    public Emprestimo buscarEntidadePorId(Long id) {
        return emprestimoRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Emprestimo nao encontrado com id: " + id));
    }

    private EmprestimoResponse toResponse(Emprestimo emprestimo) {
        // Aqui o DTO de resposta mistura dados do proprio emprestimo
        // com alguns dados uteis de Exemplar, Livro e Usuario.
        //
        // Isso e comum em APIs quando queremos entregar uma resposta
        // mais amigavel para quem consome o endpoint.
        return new EmprestimoResponse(
            emprestimo.getId(),
            emprestimo.getExemplar().getId(),
            emprestimo.getExemplar().getCodigoPatrimonio(),
            emprestimo.getExemplar().getLivro().getId(),
            emprestimo.getExemplar().getLivro().getTitulo(),
            emprestimo.getUsuario().getId(),
            emprestimo.getUsuario().getNome(),
            emprestimo.getDataEmprestimo(),
            emprestimo.getDataPrevistaDevolucao(),
            emprestimo.getDataDevolucaoReal(),
            emprestimo.getAtivo()
        );
    }
}
