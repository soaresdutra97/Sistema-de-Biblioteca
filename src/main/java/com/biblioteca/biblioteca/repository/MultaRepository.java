package com.biblioteca.biblioteca.repository;

import com.biblioteca.biblioteca.entity.Multa;
import com.biblioteca.biblioteca.enums.StatusMulta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository de multa.
//
// Ele sera usado tanto para:
// - gerar multa por atraso
// - consultar inadimplencia do usuario
// - registrar pagamento
public interface MultaRepository extends JpaRepository<Multa, Long> {
    boolean existsByEmprestimoUsuarioIdAndStatus(Long usuarioId, StatusMulta status);

    Optional<Multa> findByEmprestimoId(Long emprestimoId);

    List<Multa> findByEmprestimoUsuarioId(Long usuarioId);
}
