package com.biblioteca.biblioteca.repository;

import com.biblioteca.biblioteca.entity.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

// Repository de Autor.
//
// O padrao e o mesmo dos outros repositories:
// interface + Spring Data JPA gerando a implementacao automaticamente.
public interface AutorRepository extends JpaRepository<Autor, Long> {
    boolean existsByNome(String nome);
}
