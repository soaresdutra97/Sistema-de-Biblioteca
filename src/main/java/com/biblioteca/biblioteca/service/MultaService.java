package com.biblioteca.biblioteca.service;

import com.biblioteca.biblioteca.dto.multa.MultaResponse;
import com.biblioteca.biblioteca.dto.multa.PagamentoMultaRequest;
import com.biblioteca.biblioteca.entity.Emprestimo;
import com.biblioteca.biblioteca.entity.Multa;
import com.biblioteca.biblioteca.enums.StatusMulta;
import com.biblioteca.biblioteca.exception.RecursoNaoEncontradoException;
import com.biblioteca.biblioteca.exception.RegraDeNegocioException;
import com.biblioteca.biblioteca.repository.MultaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Service de multa.
//
// Aqui ficam as regras financeiras e de inadimplencia ligadas a atraso.
// Nesta versao:
// - multa e gerada quando a devolucao ocorre apos a data prevista
// - o valor e calculado por dia de atraso
// - usuario com multa pendente pode ser considerado inadimplente
@Service
public class MultaService {

    // Valor diario simples para estudo.
    // Mais adiante, isso poderia vir de configuracao, tabela ou regra por categoria.
    private static final BigDecimal VALOR_DIARIO_MULTA = new BigDecimal("2.50");

    private final MultaRepository multaRepository;
    private final UsuarioService usuarioService;

    public MultaService(MultaRepository multaRepository, UsuarioService usuarioService) {
        this.multaRepository = multaRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public Multa gerarSeHouverAtraso(Emprestimo emprestimo) {
        // Se ja existe multa para esse emprestimo, nao criamos outra.
        java.util.Optional<Multa> multaExistente = multaRepository.findByEmprestimoId(emprestimo.getId());
        if (multaExistente.isPresent()) {
            return multaExistente.orElseThrow();
        }

        LocalDate dataPrevista = emprestimo.getDataPrevistaDevolucao();
        LocalDate dataDevolucao = emprestimo.getDataDevolucaoReal();

        if (dataDevolucao == null || !dataDevolucao.isAfter(dataPrevista)) {
            return null;
        }

        int diasAtraso = (int) ChronoUnit.DAYS.between(dataPrevista, dataDevolucao);
        BigDecimal valor = VALOR_DIARIO_MULTA.multiply(BigDecimal.valueOf(diasAtraso));

        Multa multa = new Multa();
        multa.setEmprestimo(emprestimo);
        multa.setDiasAtraso(diasAtraso);
        multa.setValor(valor);
        multa.setStatus(StatusMulta.PENDENTE);
        multa.setDataGeracao(LocalDate.now());
        multa.setDataPagamento(null);

        return multaRepository.save(multa);
    }

    @Transactional(readOnly = true)
    public boolean usuarioPossuiMultaPendente(Long usuarioId) {
        return multaRepository.existsByEmprestimoUsuarioIdAndStatus(usuarioId, StatusMulta.PENDENTE);
    }

    @Transactional(readOnly = true)
    public List<MultaResponse> listarTodas() {
        return multaRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<MultaResponse> listarPorUsuario(Long usuarioId) {
        // Validamos se o usuario existe antes de consultar as multas.
        // Sem isso, um id inexistente apenas retornaria lista vazia,
        // o que seria ambiguo para quem consome a API.
        usuarioService.buscarEntidadePorId(usuarioId);

        return multaRepository.findByEmprestimoUsuarioId(usuarioId).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MultaResponse buscarPorId(Long id) {
        return toResponse(buscarEntidadePorId(id));
    }

    @Transactional
    public MultaResponse pagar(Long multaId, PagamentoMultaRequest request) {
        Multa multa = buscarEntidadePorId(multaId);

        if (multa.getStatus() == StatusMulta.PAGA) {
            throw new RegraDeNegocioException("Esta multa ja foi paga.");
        }

        if (multa.getStatus() == StatusMulta.CANCELADA) {
            throw new RegraDeNegocioException("Nao e permitido pagar uma multa cancelada.");
        }

        if (request.dataPagamento().isBefore(multa.getDataGeracao())) {
            throw new RegraDeNegocioException("A data de pagamento nao pode ser anterior a data de geracao da multa.");
        }

        multa.setStatus(StatusMulta.PAGA);
        multa.setDataPagamento(request.dataPagamento());

        return toResponse(multaRepository.save(multa));
    }

    @Transactional(readOnly = true)
    public Multa buscarEntidadePorId(Long id) {
        return multaRepository.findById(id)
            .orElseThrow(() -> new RecursoNaoEncontradoException("Multa nao encontrada com id: " + id));
    }

    private MultaResponse toResponse(Multa multa) {
        return new MultaResponse(
            multa.getId(),
            multa.getEmprestimo().getId(),
            multa.getEmprestimo().getUsuario().getId(),
            multa.getEmprestimo().getUsuario().getNome(),
            multa.getDiasAtraso(),
            multa.getValor(),
            multa.getStatus(),
            multa.getDataGeracao(),
            multa.getDataPagamento()
        );
    }
}
