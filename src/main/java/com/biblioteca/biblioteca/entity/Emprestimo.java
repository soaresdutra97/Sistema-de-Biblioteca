package com.biblioteca.biblioteca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Esta classe representa o ato de emprestar um livro para um usuario.
//
// Isso e importante do ponto de vista de modelagem:
// Emprestimo nao e apenas uma classe "auxiliar".
// Ele e uma entidade de negocio real.
//
// Pense assim:
// - Livro existe no sistema como obra bibliografica
// - Exemplar existe como copia fisica
// - Usuario existe no sistema
// - Emprestimo registra a relacao de negocio entre usuario e exemplar em um momento do tempo
//
// E no emprestimo que vivem informacoes como:
// - data do emprestimo
// - data prevista de devolucao
// - data real de devolucao
// - status atual
//
// Em sistemas reais, essa entidade costuma concentrar boa parte das regras:
// - um livro pode ou nao estar disponivel
// - um usuario pode ou nao poder emprestar
// - um emprestimo pode estar atrasado
// - uma devolucao pode atualizar o estoque
@Entity
@Table(name = "emprestimos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// Excluimos exemplar e usuario do toString para evitar saidas muito grandes
// ou ciclos futuros quando os relacionamentos crescerem.
@ToString(exclude = {"exemplar", "usuario"})
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Aqui aparece um conceito novo e muito importante:
    // relacionamento entre entidades.
    //
    // @ManyToOne significa:
    // muitos emprestimos podem estar associados ao mesmo exemplar ao longo do tempo.
    //
    // Exemplo:
    // um mesmo exemplar pode ter sido emprestado varias vezes em periodos diferentes.
    //
    // Isso NAO e injecao de dependencia do Spring.
    // Isso e um relacionamento de dados no modelo JPA.
    //
    // Ou seja:
    // - Spring DI liga beans como controller -> service -> repository
    // - JPA relaciona entidades como emprestimo -> exemplar -> usuario
    @ManyToOne(fetch = FetchType.LAZY)

    // @JoinColumn define a coluna de chave estrangeira na tabela emprestimos.
    // Aqui, a tabela emprestimos tera uma coluna chamada exemplar_id.
    //
    // Em banco relacional, isso indica para qual exemplar este emprestimo aponta.
    @JoinColumn(name = "exemplar_id", nullable = false)
    private Exemplar exemplar;

    // Muitos emprestimos tambem podem pertencer ao mesmo usuario.
    // Um usuario pode fazer varios emprestimos ao longo do tempo.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Data em que o emprestimo foi realizado.
    @Column(name = "data_emprestimo", nullable = false)
    private LocalDate dataEmprestimo;

    // Data esperada para devolucao.
    @Column(name = "data_prevista_devolucao", nullable = false)
    private LocalDate dataPrevistaDevolucao;

    // Data real em que o livro foi devolvido.
    // Este campo pode ser nulo no inicio, porque o livro ainda nao foi devolvido.
    @Column(name = "data_devolucao_real")
    private LocalDate dataDevolucaoReal;

    // Campo simples para indicar se o emprestimo esta ativo.
    // Mais adiante, podemos evoluir isso para um enum, por exemplo:
    // - ATIVO
    // - DEVOLVIDO
    // - ATRASADO
    //
    // Por enquanto vamos manter Boolean para reduzir complexidade inicial.
    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    // Lombok gera getters, setters, construtores e toString.
    //
    // Conhecimento futuro importante:
    // em entidades com relacoes bidirecionais, serializacao JSON pode causar
    // problemas como recursao infinita. Quando chegarmos na API, eu vou te mostrar
    // por que normalmente nao devolvemos entities diretamente no controller.
}
