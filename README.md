# Biblioteca v1

API REST de biblioteca desenvolvida com `Java`, `Spring Boot`, `Spring Web`, `Spring Data JPA`, `Hibernate`, `Bean Validation`, `Lombok` e banco `H2`.

Esta versao do projeto foi construída com foco em aprendizado de:

- Java aplicado a backend
- arquitetura em camadas
- Spring Core e Spring Boot
- modelagem de domínio
- JPA e relacionamentos
- boas práticas de organização de código
- validação e tratamento de erros

## Visao Geral

O sistema permite gerenciar:

- livros
- autores
- editoras
- categorias
- exemplares fisicos
- usuarios
- emprestimos
- devolucoes

O projeto separa de forma intencional:

- `Livro`: obra bibliografica
- `Exemplar`: copia fisica de um livro
- `Emprestimo`: transacao de emprestimo e devolucao

Essa modelagem deixa o sistema mais proximo de uma biblioteca real.

## Objetivo Do Projeto

Este projeto foi usado como base de estudo para construir uma API com Spring Boot aplicando:

- orientacao a objetos
- responsabilidade unica
- injecao de dependencia
- inversao de controle
- separacao entre camadas
- uso de DTOs
- tratamento global de exceptions

## Tecnologias Utilizadas

- `Java 17`
- `Spring Boot`
- `Spring Web MVC`
- `Spring Data JPA`
- `Hibernate`
- `H2 Database`
- `Bean Validation`
- `Lombok`
- `Maven`

## Arquitetura

O projeto segue uma arquitetura em camadas.

### Estrutura De Pastas

```text
src/main/java/com/biblioteca/biblioteca
├── config
├── controller
├── dto
├── entity
├── enums
├── exception
├── repository
├── service
└── Treinamento
```

### Papel De Cada Camada

- `controller`
  Recebe requisicoes HTTP e devolve respostas HTTP.

- `service`
  Centraliza regras de negocio e orquestra a aplicacao.

- `repository`
  Faz o acesso a dados usando Spring Data JPA.

- `entity`
  Representa o modelo persistido no banco.

- `dto`
  Define os contratos de entrada e saida da API.

- `exception`
  Centraliza tratamento de erros e respostas padronizadas.

- `enums`
  Armazena tipos controlados do dominio, como status de exemplar.

- `Treinamento`
  Contem classes didaticas de estudo separadas da aplicacao principal.

## Modelagem Do Dominio

### Entidades Principais

- `Livro`
  Representa a obra bibliografica.

- `Autor`
  Representa o autor do livro.

- `Editora`
  Representa a editora responsavel pela publicacao.

- `Categoria`
  Representa a classificacao tematica do livro.

- `Exemplar`
  Representa uma copia fisica de um livro.

- `Usuario`
  Representa a pessoa apta a pegar livros emprestados.

- `Emprestimo`
  Representa a transacao de emprestimo e devolucao.

### Relacionamentos

- `Livro -> Autor`
  `ManyToOne`

- `Livro -> Editora`
  `ManyToOne`

- `Livro -> Categoria`
  `ManyToMany`

- `Exemplar -> Livro`
  `ManyToOne`

- `Emprestimo -> Exemplar`
  `ManyToOne`

- `Emprestimo -> Usuario`
  `ManyToOne`

### Evolucao Importante Do Dominio

Inicialmente, o sistema trabalhava apenas com `Livro` e `quantidadeDisponivel`.

Depois, o dominio foi evoluido para um modelo mais realista:

- `Livro` representa a obra
- `Exemplar` representa a unidade fisica

Com isso:

- o emprestimo passou a ocorrer sobre um exemplar especifico
- o sistema passou a controlar status do exemplar
- o acervo ficou mais proximo de um sistema real

## Funcionalidades Implementadas

### Livros

- cadastrar livro
- listar livros
- buscar livro por id
- atualizar livro
- remover livro

### Autores

- cadastrar autor
- listar autores
- buscar autor por id
- atualizar autor
- remover autor

### Editoras

- cadastrar editora
- listar editoras
- buscar editora por id
- atualizar editora
- remover editora

### Categorias

- cadastrar categoria
- listar categorias
- buscar categoria por id
- atualizar categoria
- remover categoria

### Exemplares

- cadastrar exemplar
- listar exemplares
- listar exemplares por livro
- buscar exemplar por id
- atualizar exemplar
- remover exemplar

### Usuarios

- cadastrar usuario
- listar usuarios
- buscar usuario por id
- atualizar usuario
- remover usuario

### Emprestimos

- registrar emprestimo
- listar emprestimos
- buscar emprestimo por id
- registrar devolucao

## Regras De Negocio Ja Aplicadas

- nao permite ISBN duplicado
- nao permite email duplicado
- nao permite documento duplicado
- nao permite autor com nome duplicado
- nao permite categoria com nome duplicado
- nao permite editora com nome duplicado
- nao permite codigo patrimonial duplicado para exemplar
- usuario inativo nao pode realizar emprestimo
- so empresta quando existe exemplar com status `DISPONIVEL`
- ao emprestar:
  - um exemplar disponivel e selecionado
  - o status do exemplar muda para `EMPRESTADO`
- ao devolver:
  - o emprestimo e encerrado
  - o status do exemplar volta para `DISPONIVEL`

## Boas Praticas Aplicadas

- arquitetura em camadas
- DTOs separados de entities
- uso de `record` para DTOs
- validacao de entrada com `Bean Validation`
- injecao de dependencia por construtor
- uso de `private final` para dependencias obrigatorias
- regras de negocio centralizadas em `service`
- tratamento global de erros com `@RestControllerAdvice`
- exceptions especificas para negocio e recurso nao encontrado
- uso de `@Transactional`
- uso de `Lombok` para reduzir boilerplate
- modelagem de dominio evolutiva

## Conceitos De Spring Aplicados

### Bean

Classes anotadas com Spring, como:

- `@RestController`
- `@Service`
- repositories do Spring Data
- `@RestControllerAdvice`

sao gerenciadas pelo container do Spring.

### Injecao De Dependencia

As dependencias sao recebidas por construtor.

Exemplo conceitual:

- controller depende de service
- service depende de repository

As classes nao criam suas dependencias manualmente com `new`.

### Inversao De Controle

O Spring cria e conecta os objetos principais da aplicacao.

Ou seja:

- a aplicacao declara do que precisa
- o Spring decide como criar e montar os beans

## Validacoes

As validacoes de formato e obrigatoriedade ficam nos DTOs, com anotacoes como:

- `@NotBlank`
- `@NotNull`
- `@Email`
- `@Pattern`
- `@Size`
- `@Min`

Exemplos:

- email valido
- documento com 11 digitos
- titulo obrigatorio
- categoria obrigatoria

## Tratamento De Erros

O projeto possui tratamento global de erros com:

- `RecursoNaoEncontradoException`
- `RegraDeNegocioException`
- `GlobalExceptionHandler`

Isso permite devolver respostas padronizadas para:

- recurso nao encontrado
- regra de negocio violada
- erro de validacao
- erro interno inesperado

## Banco De Dados

O projeto usa `H2` para facilitar estudo e testes locais.

Configuracoes atuais incluem:

- banco em memoria
- console H2 habilitado
- JPA/Hibernate configurado
- exibicao de SQL no log

### H2 Console

Quando a aplicacao estiver rodando:

- URL: `http://localhost:8080/h2-console`

Configuracao padrao:

- JDBC URL: `jdbc:h2:mem:biblioteca`
- User Name: `sa`
- Password: vazio

## Endpoints Principais

### Livros

- `POST /livros`
- `GET /livros`
- `GET /livros/{id}`
- `PUT /livros/{id}`
- `DELETE /livros/{id}`

### Autores

- `POST /autores`
- `GET /autores`
- `GET /autores/{id}`
- `PUT /autores/{id}`
- `DELETE /autores/{id}`

### Editoras

- `POST /editoras`
- `GET /editoras`
- `GET /editoras/{id}`
- `PUT /editoras/{id}`
- `DELETE /editoras/{id}`

### Categorias

- `POST /categorias`
- `GET /categorias`
- `GET /categorias/{id}`
- `PUT /categorias/{id}`
- `DELETE /categorias/{id}`

### Exemplares

- `POST /exemplares`
- `GET /exemplares`
- `GET /exemplares?livroId={id}`
- `GET /exemplares/{id}`
- `PUT /exemplares/{id}`
- `DELETE /exemplares/{id}`

### Usuarios

- `POST /usuarios`
- `GET /usuarios`
- `GET /usuarios/{id}`
- `PUT /usuarios/{id}`
- `DELETE /usuarios/{id}`

### Emprestimos

- `POST /emprestimos`
- `GET /emprestimos`
- `GET /emprestimos/{id}`
- `PATCH /emprestimos/{id}/devolucao`

## Como Rodar O Projeto

### Pre-requisitos

- `JDK 17`
- `Maven` ou uso do `Maven Wrapper`

### Executando

No terminal:

```cmd
mvnw.cmd spring-boot:run
```

Ou pela IDE, executando a classe:

- `BibliotecaApplication`

## Exemplo De Fluxo De Uso

1. cadastrar autor
2. cadastrar editora
3. cadastrar categoria
4. cadastrar livro
5. cadastrar exemplar
6. cadastrar usuario
7. registrar emprestimo
8. registrar devolucao

## Limites Atuais Da V1

Esta versao ainda pode evoluir em varios pontos, por exemplo:

- autenticacao e autorizacao
- testes automatizados
- paginacao
- filtros de busca
- Swagger/OpenAPI
- PostgreSQL
- Flyway ou Liquibase
- reservas
- multas
- renovacao de emprestimos
- historico detalhado do usuario

## Proximos Passos Possiveis

- adicionar reserva de livros
- adicionar multa por atraso
- adicionar autenticacao com Spring Security e JWT
- trocar H2 por PostgreSQL
- criar testes unitarios e de integracao
- adicionar documentacao da API com Swagger