# Guia Git

Este guia foi criado para servir como referencia pratica de uso do Git no projeto.

Ele cobre:

- criacao do repositorio local
- criacao do repositorio no GitHub
- primeiro push
- criacao de branch de feature
- commits
- push de branch
- abertura de Pull Request

## 1. Criar O Repositorio Local

Se o projeto ainda nao existe:

```bash
mkdir biblioteca
cd biblioteca
```

Se o projeto ja existe, entre na pasta dele:

```bash
cd caminho/do/projeto
```

Inicialize o Git:

```bash
git init
```

Verifique o estado:

```bash
git status
```

## 2. Fazer O Primeiro Commit

Adicionar os arquivos:

```bash
git add .
```

Criar o primeiro commit:

```bash
git commit -m "feat: inicia biblioteca v1"
```

## 3. Criar O Repositorio No GitHub

No GitHub:

1. clique em `New repository`
2. informe o nome do repositorio
3. escolha se sera publico ou privado
4. crie o repositorio

### Recomendacao

Se possivel, crie o repositorio sem marcar:

- `Add a README file`
- `Add .gitignore`
- `Choose a license`

Isso evita conflito no primeiro push.

## 4. Conectar O Repositorio Local Ao GitHub

Copie a URL do repositorio criado e rode:

```bash
git remote add origin https://github.com/seu-usuario/seu-repo.git
```

Exemplo:

```bash
git remote add origin https://github.com/soaresdutra97/Sistema-de-Biblioteca.git
```

## 5. Ajustar A Branch Principal

Renomeie a branch principal para `main`:

```bash
git branch -M main
```

## 6. Fazer O Primeiro Push

```bash
git push -u origin main
```

### Se der erro porque o remoto ja tem arquivos

Se o GitHub ja tiver um `README` ou outro commit inicial:

```bash
git pull origin main --allow-unrelated-histories
git push -u origin main
```

## 7. Fluxo Correto Para Criar Uma Nova Feature

Antes de criar uma branch nova, sempre volte para a `main` e atualize:

```bash
git checkout main
git pull origin main
```

Agora crie a branch da feature:

```bash
git checkout -b feat/nome-da-feature
```

Exemplo:

```bash
git checkout -b feat/reserva-livros
```

## 8. Desenvolver A Feature

Depois de criar a branch:

1. altere os arquivos
2. confira o estado do repositorio

```bash
git status
```

## 9. Adicionar As Alteracoes

Para adicionar tudo:

```bash
git add .
```

Para adicionar um arquivo especifico:

```bash
git add caminho/do/arquivo
```

## 10. Criar O Commit

Exemplo:

```bash
git commit -m "feat: adiciona reserva de livros"
```

## 11. Subir A Branch Da Feature

Na primeira vez:

```bash
git push -u origin feat/reserva-livros
```

Depois disso, se continuar trabalhando na mesma branch:

```bash
git push
```

## 12. Abrir O Pull Request

Depois do push:

1. abra o GitHub
2. entre no repositorio
3. clique em `Compare & pull request`
4. confira:
   - `base`: `main`
   - `compare`: sua branch de feature
5. escreva o titulo do PR
6. escreva a descricao
7. clique em `Create pull request`

## 13. Exemplo De Titulo E Descricao De PR

### Titulo

```text
feat: adiciona reserva de livros
```

### Descricao

```text
## O que foi feito
- adiciona entidade de reserva
- cria service e controller
- adiciona validacoes de disponibilidade

## Motivo
Permitir que usuarios reservem livros indisponiveis.

## Observacoes
- sem notificacao nesta etapa
- sem testes automatizados nesta versao
```

## 14. Se Pedirem Ajustes No PR

Continue na mesma branch.

Ajuste o codigo e rode:

```bash
git add .
git commit -m "fix: ajusta regra da reserva"
git push
```

O Pull Request sera atualizado automaticamente.

## 15. Depois Do Merge

Depois que a branch for aprovada e integrada na `main`:

```bash
git checkout main
git pull origin main
```

## 16. Apagar Branch Antiga

### Apagar branch local

```bash
git branch -d feat/reserva-livros
```

### Apagar branch remota

```bash
git push origin --delete feat/reserva-livros
```

## 17. Padrões Recomendados De Nome

### Branches

- `feat/nome`
- `fix/nome`
- `refactor/nome`
- `docs/nome`
- `test/nome`

Exemplos:

- `feat/exemplares`
- `feat/reserva-livros`
- `fix/filtro-por-livro`
- `docs/readme-v2`

### Commits

- `feat: adiciona cadastro de exemplares`
- `fix: corrige filtro por livroId`
- `refactor: reorganiza regra de emprestimo`
- `docs: atualiza readme da biblioteca v1`

## 18. Fluxo Resumido

### Projeto novo

```bash
git init
git add .
git commit -m "feat: inicia biblioteca v1"
git remote add origin URL_DO_REPOSITORIO
git branch -M main
git push -u origin main
```

### Nova feature

```bash
git checkout main
git pull origin main
git checkout -b feat/nome-da-feature
git add .
git commit -m "feat: descricao da feature"
git push -u origin feat/nome-da-feature
```

Depois:

- abrir Pull Request no GitHub

## 19. Regra De Ouro

Nunca comece uma nova feature direto na `main`.

O fluxo certo e:

1. atualizar `main`
2. criar branch nova
3. desenvolver
4. commitar
5. dar push
6. abrir PR
