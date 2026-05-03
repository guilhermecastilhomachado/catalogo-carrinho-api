# API de Catálogo e Carrinho (Spring Boot)

[![Java](https://img.shields.io/badge/Java-17-informational)](#)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-brightgreen)](#)
[![Database](https://img.shields.io/badge/Database-MySQL%208.4-blue)](#)
[![Docs](https://img.shields.io/badge/Swagger-OpenAPI-green)](#)

API REST desenvolvida como projeto de portfólio/estudos para praticar **boas práticas de back-end** com **Java**, **Spring Boot**, **JPA**, **validações**, **tratamento global de erros**, **testes automatizados** e **Docker**.

---

## Sumario

- [Objetivo](#objetivo)
- [Tecnologias utilizadas](#tecnologias-utilizadas)
- [Funcionalidades](#funcionalidades)
- [Modelagem do dominio (resumo)](#modelagem-do-dominio-resumo)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Documentacao (Swagger)](#documentacao-swagger)
- [Endpoints principais](#endpoints-principais)
- [Como executar](#como-executar)
  - [Clonando o repositorio](#clonando-o-repositorio)
  - [Pre-requisitos](#pre-requisitos)
  - [Subindo o MySQL com Docker](#subindo-o-mysql-com-docker)
  - [Executando a aplicacao](#executando-a-aplicacao)
  - [Executando com perfil de testes (H2 em memoria)](#executando-com-perfil-de-testes-h2-em-memoria)
- [Perfis de configuracao](#perfis-de-configuracao)
- [Testes automatizados](#testes-automatizados)
- [Tratamento de erros](#tratamento-de-erros)
- [Melhorias futuras (roadmap)](#melhorias-futuras-roadmap)

---

## Objetivo

Simular um **catálogo de produtos** com **carrinho de compras** e um **checkout simplificado**, com regras de negócio típicas:

- cadastro e gerenciamento de **categorias** e **produtos**
- criação de **carrinho**
- adicionar/remover itens e atualizar quantidade
- **finalização (checkout)** com baixa de estoque

---

## Tecnologias utilizadas

- **Java 17**
- **Spring Boot 4.0.6**
- Spring Web (WebMVC)
- Spring Data JPA
- Bean Validation (Jakarta Validation)
- MySQL 8.4
- Docker + Docker Compose
- Lombok
- Swagger / OpenAPI (springdoc)
- H2 Database (perfil de testes)
- JUnit 5
- Mockito

---

## Funcionalidades

### Catálogo
- CRUD de **Categorias**
- CRUD de **Produtos**
- Listagem de produtos por **categoria**

### Carrinho
- Criação de carrinho
- Adição de itens ao carrinho
- Atualização de quantidade de itens
- Remoção de itens
- Checkout simplificado (finalização)
- Atualização do estoque após finalização
- Paginação e ordenação em listagens
- Persistência do checkout em histórico de pedidos
- Consulta de pedidos por ID e por carrinho de origem

### Qualidade
- Validações com mensagens amigáveis
- Tratamento global de exceções
- Perfis por ambiente: `dev`, `test`, `prod`
- Testes unitários e teste de integração do fluxo de carrinho

---

## Modelagem do dominio (resumo)

Relações principais:

- **Categoria (1) -> (N) Produto**
- **Carrinho (1) -> (N) ItemCarrinho**
- **ItemCarrinho (N) -> (1) Produto**

Status do carrinho:

- `ABERTO`
- `FINALIZADO`

> Observação: o `ItemCarrinho` calcula automaticamente o `subtotal` em `@PrePersist/@PreUpdate`, e o `Carrinho` inicia com `status=ABERTO` e `valorTotal=0` em `@PrePersist`.

---

## Estrutura do projeto

```text
src/main/java/br/ufu/catalogocarrinhoapi
├── controlador   # Controllers REST (rotas)
├── dto           # DTOs de entrada para operações do carrinho
├── enumeracao    # Enums (ex.: StatusCarrinho)
├── excecao       # Exceptions + handler global e payload de erro
├── modelo        # Entidades JPA (Categoria, Produto, Carrinho, ItemCarrinho)
├── repositorio   # Repositórios Spring Data JPA
└── servico       # Regras de negócio
```

---

## Documentacao (Swagger)

A API expõe documentação interativa via Swagger UI:

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`

---

## Endpoints principais

> Base URL local: `http://localhost:8080`

### Categorias
| Método | Rota | Descrição |
|-------:|------|-----------|
| POST | `/categorias` | Cria uma categoria |
| GET | `/categorias` | Lista categorias |
| GET | `/categorias/{id}` | Busca categoria por ID |
| PUT | `/categorias/{id}` | Atualiza categoria |
| DELETE | `/categorias/{id}` | Remove categoria |

### Produtos
| Método | Rota | Descrição |
|-------:|------|-----------|
| POST | `/produtos` | Cria um produto |
| GET | `/produtos` | Lista produtos |
| GET | `/produtos/{id}` | Busca produto por ID |
| PUT | `/produtos/{id}` | Atualiza produto |
| DELETE | `/produtos/{id}` | Remove produto |
| GET | `/produtos/categoria/{categoriaId}` | Lista produtos por categoria |

### Carrinhos
| Método | Rota | Descrição |
|-------:|------|-----------|
| POST | `/carrinhos` | Cria um carrinho |
| GET | `/carrinhos` | Lista carrinhos |
| GET | `/carrinhos/{id}` | Busca carrinho por ID |
| POST | `/carrinhos/{carrinhoId}/itens` | Adiciona item ao carrinho |
| PUT | `/carrinhos/{carrinhoId}/itens/{itemId}` | Atualiza quantidade de um item |
| DELETE | `/carrinhos/{carrinhoId}/itens/{itemId}` | Remove item do carrinho |
| POST | `/carrinhos/{carrinhoId}/checkout` | Finaliza carrinho (checkout) |

### Pedidos
| Método | Rota                             | Descrição                         |
|-------:|----------------------------------|-----------------------------------|
|    GET | `/pedidos`                       | Lista pedidos                     |
|    GET | `/pedidos/{id}`                  | Busca pedidos por ID              |
|    GET | `/pedidos/carrinho/{carrinhoId}` | Busca pedidos pelo ID do carrinho |

---

## Como executar

### Clonando o repositorio

```powershell
git clone https://github.com/<seu-usuario>/catalogo-carrinho-api.git
Set-Location catalogo-carrinho-api
```

### Pre-requisitos

- Java 17 instalado
- Docker Desktop (para subir o MySQL)
- Git (opcional)

> Você **não precisa** ter Maven instalado: o projeto usa **Maven Wrapper** (`mvnw` / `mvnw.cmd`).

### Subindo o MySQL com Docker

O `compose.yaml` sobe um MySQL com:

- **DB:** `catalogo_carrinho`
- **usuário:** `root`
- **senha:** `root`
- **porta no Windows/host:** `3307` (mapeada para `3306` no container)

No PowerShell, na raiz do projeto:

```powershell
docker compose up -d
```

Para parar e remover containers/volume:

```powershell
docker compose down -v
```

### Executando a aplicacao

O perfil padrão é **`dev`** (configurado em `application.properties`). Esse perfil aponta para o MySQL do Docker na porta `3307`.

Opção A) Rodar pela IDE

- Execute a classe `CatalogoCarrinhoApiApplication`.

Opção B) Rodar via terminal (PowerShell)

```powershell
.\mvnw.cmd spring-boot:run
```

Depois acesse:

- Swagger UI: `http://localhost:8080/swagger-ui.html`

### Executando com perfil de testes (H2 em memoria)

O perfil `test` usa H2 em memória (sem Docker/MySQL).

PowerShell:

```powershell
$env:APP_PROFILE = "test"
.\mvnw.cmd spring-boot:run
```

Para voltar ao perfil padrão (dev), basta fechar o terminal (ou limpar a variável):

```powershell
Remove-Item Env:APP_PROFILE
```

---

## Perfis de configuracao

O projeto usa `spring.profiles.active=${APP_PROFILE:dev}`.

- **`dev`**: ambiente local com MySQL via Docker (`application-dev.properties`)
- **`test`**: H2 em memória para testes automatizados (`application-test.properties`)
- **`prod`**: preparado para usar variáveis de ambiente (`application-prod.properties`)

Variáveis esperadas em `prod`:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`

---

## Testes automatizados

O projeto possui testes para validar regras de negócio e o fluxo principal da API.

- **Unitários** (serviços) com JUnit 5 + Mockito
- **Integração** do fluxo completo do carrinho (criando categoria/produto, adicionando item e realizando checkout)

Executar testes no Windows (PowerShell):

```powershell
.\mvnw.cmd test
```

---

## Tratamento de erros

A API possui um handler global (`TratadorGlobalExcecao`) para padronizar respostas de erro.

### Exemplos de resposta

**Regra de negócio violada (400)**

Ex.: tentar finalizar um carrinho vazio.

```json
{
  "dataHora": "2026-05-01T12:34:56.789",
  "status": 400,
  "erro": "Regra de negocio violada",
  "mensagem": "Nao e possivel finalizar um carrinho vazio.",
  "caminho": "/carrinhos/1/checkout",
  "campos": null
}
```

**Erro de validação (400)**

Quando algum campo falha em `@Valid`, a resposta inclui um mapa `campos` com as mensagens por atributo.

```json
{
  "dataHora": "2026-05-01T12:34:56.789",
  "status": 400,
  "erro": "Erro de validacao",
  "mensagem": "Um ou mais campos estao invalidos.",
  "caminho": "/produtos",
  "campos": {
    "nome": "O nome do produto e obrigatorio.",
    "preco": "O preco do produto e obrigatorio."
  }
}
```

**Recurso não encontrado (404)**

```json
{
  "dataHora": "2026-05-01T12:34:56.789",
  "status": 404,
  "erro": "Recurso nao encontrado",
  "mensagem": "...",
  "caminho": "/produtos/999",
  "campos": null
}
```

---

## Melhorias futuras (roadmap)

- Autenticação e autorização (ex.: Spring Security + JWT)
- Upload de imagem de produto
- Observabilidade (logs estruturados, métricas)

---

Projeto desenvolvido para fins acadêmicos/portfólio.