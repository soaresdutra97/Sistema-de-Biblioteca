package com.biblioteca.biblioteca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

// Esta classe representa a entidade Livro no dominio da aplicacao.
//
// "Dominio" aqui significa o modelo de negocio que estamos tentando representar.
// Em uma biblioteca real, Livro e uma entidade importante do sistema.
//
// Esta classe sera mapeada para uma tabela no banco de dados pelo JPA/Hibernate.
// Ou seja:
// - a classe vira uma tabela
// - os atributos viram colunas
// - cada objeto Livro vira uma linha da tabela
//
// Importante:
// esta classe NAO e um bean de service ou controller.
// Ela nao existe para conter regra de negocio do Spring.
// Ela existe para representar e persistir dados do dominio.
//
// Mais adiante, quando criarmos DTOs, voce vai ver uma diferenca importante:
// - Entity: representa o dado persistido no banco
// - DTO: representa o dado trafegado na API
//
// Em projetos reais, geralmente evitamos expor a entity diretamente na API.
// Por isso depois criaremos classes separadas para entrada e saida de dados.
@Entity

// @Table permite definir explicitamente o nome da tabela no banco.
// Se nao colocassemos isso, o JPA tentaria inferir um nome automaticamente.
// Declarar explicitamente ajuda no aprendizado e reduz ambiguidades.
@Table(name = "livros")
@Getter
@Setter

// Lombok gera automaticamente um construtor sem argumentos.
// Isso e importante porque o JPA precisa conseguir instanciar a entidade.
//
// Sem esse construtor, voce frequentemente teria problemas quando o Hibernate
// tentasse criar objetos Livro ao ler dados do banco.
@NoArgsConstructor

// Lombok gera automaticamente um construtor com todos os atributos da classe.
// Isso reduz codigo repetitivo e facilita criar objetos em testes e exemplos.
//
// Mais adiante, em projetos maiores, nem sempre queremos expor um construtor
// com todos os campos, principalmente quando o id e gerado pelo banco.
// Ainda assim, para estudo, ele e util para voce entender a ideia.
@AllArgsConstructor

// Lombok tambem gera o metodo toString automaticamente.
// Isso ajuda bastante em depuracao e logs simples.
//
// O mesmo cuidado continua valendo:
// em entidades com muitos relacionamentos, um toString automatico pode gerar
// saidas grandes demais ou ate loops de referencia.
@ToString(exclude = {"autor", "editora", "categorias"})
public class Livro {

    // @Id marca o atributo que identifica unicamente cada registro da tabela.
    // Em termos de banco, isso vira a chave primaria.
    @Id

    // @GeneratedValue informa que o valor do id sera gerado automaticamente.
    // GenerationType.IDENTITY costuma significar que o banco controla esse valor,
    // normalmente com auto incremento.
    //
    // Mais adiante voce tambem vai conhecer outras estrategias, como:
    // - AUTO
    // - SEQUENCE
    // - TABLE
    //
    // Para um CRUD simples com H2, IDENTITY e uma escolha direta e didatica.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Column permite configurar detalhes da coluna no banco.
    //
    // name = "titulo"
    // define o nome da coluna.
    //
    // nullable = false
    // indica que esta coluna nao deve aceitar nulo no banco.
    //
    // length = 150
    // define o tamanho maximo da coluna de texto.
    //
    // Observacao importante:
    // isso atua no mapeamento com o banco, mas nao substitui completamente validacoes
    // de entrada na API. Mais adiante vamos aprender validacoes com Bean Validation,
    // como @NotBlank e @Size, geralmente aplicadas em DTOs.
    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    // Agora Livro se relaciona com Autor como entidade.
    //
    // Muitos livros podem pertencer ao mesmo autor.
    // Por isso usamos ManyToOne.
    //
    // fetch = LAZY evita carregar o autor imediatamente em todos os cenarios.
    // O JPA pode adiar esse carregamento ate que ele seja realmente necessario.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id", nullable = false)
    private Autor autor;

    // A editora segue a mesma logica:
    // muitos livros podem ser publicados pela mesma editora.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editora_id", nullable = false)
    private Editora editora;

    @Column(name = "isbn", nullable = false, unique = true, length = 20)
    private String isbn;

    @Column(name = "ano_publicacao", nullable = false)
    private Integer anoPublicacao;

    // Este campo passa a funcionar como um resumo da disponibilidade.
    //
    // Agora a fonte principal da verdade sao os Exemplares e seus status.
    // Mesmo assim, manter esse numero pode ser util para respostas rapidas
    // e para facilitar a visualizacao do acervo.
    //
    // Em uma evolucao futura, poderiamos remover esse campo e calcular
    // a disponibilidade somente por consulta aos exemplares.
    @Column(name = "quantidade_disponivel", nullable = false)
    private Integer quantidadeDisponivel;

    // Livro e Categoria formam um relacionamento ManyToMany.
    //
    // Um livro pode estar em varias categorias.
    // Uma categoria pode estar em varios livros.
    //
    // O relacionamento ManyToMany precisa de uma tabela intermediaria.
    // Aqui ela se chamara livro_categoria.
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "livro_categoria",
        joinColumns = @JoinColumn(name = "livro_id"),
        inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private Set<Categoria> categorias = new LinkedHashSet<>();

    // Os metodos abaixo nao aparecem mais escritos manualmente porque o Lombok
    // os gera em tempo de compilacao:
    // - getters
    // - setters
    // - construtor sem argumentos
    // - construtor com todos os argumentos
    // - toString
    //
    // Isso diminui boilerplate, deixando a classe mais enxuta.
}
