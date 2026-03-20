package com.biblioteca.biblioteca.repository;

import com.biblioteca.biblioteca.entity.Exemplar;
import com.biblioteca.biblioteca.enums.StatusExemplar;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository de Exemplar.
//
// Agora ele passa a ser fundamental para o fluxo de emprestimo,
// porque a disponibilidade deixa de ser um numero solto e passa a depender
// do status de cada copia fisica.
public interface ExemplarRepository extends JpaRepository<Exemplar, Long> {
    boolean existsByCodigoPatrimonio(String codigoPatrimonio);

    Optional<Exemplar> findFirstByLivroIdAndStatus(Long livroId, StatusExemplar status);

    List<Exemplar> findByLivroId(Long livroId);
}
