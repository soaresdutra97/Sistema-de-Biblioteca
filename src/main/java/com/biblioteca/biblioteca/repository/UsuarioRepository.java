package com.biblioteca.biblioteca.repository;

import com.biblioteca.biblioteca.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

// Este repository cuida do acesso aos dados da entidade Usuario.
//
// Ele segue a mesma ideia do LivroRepository:
// o Spring Data cria uma implementacao concreta automaticamente.
//
// Isso costuma gerar uma duvida comum:
// "Mas onde esta a classe que implementa essa interface?"
//
// Na maioria dos casos, voce nao escreve essa classe.
// O proprio Spring gera internamente a implementacao e registra como bean.
//
// Por isso, quando chegarmos ao service, sera possivel fazer algo como:
//
// private final UsuarioRepository usuarioRepository;
//
// E o Spring injetara o objeto pronto.
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Como email e documento serao campos importantes na regra de negocio,
    // ja e comum imaginar metodos futuros como:
    // boolean existsByEmail(String email);
    // boolean existsByDocumento(String documento);
    //
    // Esses metodos ainda nao sao obrigatorios agora,
    // mas vao fazer sentido quando criarmos UsuarioService.
    boolean existsByEmail(String email);

    boolean existsByDocumento(String documento);
}
