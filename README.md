# Neosrate

API REST de uma plataforma social estilo Reddit: usuários criam comunidades, publicam posts (com upload de mídia para S3), comentam e curtem. Autenticação stateless por JWT.

## Stack

- Java 21 + Spring Boot 3
- MySQL 8 (Spring Data JPA / Hibernate)
- Spring Security + `java-jwt` (Auth0)
- AWS S3 SDK (upload de mídia)
- ModelMapper
- Jakarta Validation + Hibernate Validator
- Maven

## Estrutura

```
src/main/java/com/neosrate/neosrate/
├── configuration/   Beans Spring (Security, S3, CORS)
├── controller/      Endpoints REST
├── service/         Lógica de negócio
├── repository/      Spring Data JPA
└── data/
    ├── dto/         Records de input/output (post, user, Community)
    ├── enums/
    └── model/       JPA entities
```

## Setup

Requer Docker e Docker Compose.

```bash
cp .env.example .env
# ajustar API_SECURITY_TOKEN_SECRET e credenciais AWS
docker compose up --build
```

Sobe MySQL 8 + API. API em `http://localhost:8080`.

## Variáveis de ambiente

Definidas no `.env` (consumidas pelo compose; o datasource aponta para o serviço `db`):

| Variável | Descrição |
|---|---|
| `API_SECURITY_TOKEN_SECRET` | segredo do JWT |
| `AWS_ACCESS_KEY` / `AWS_SECRET_KEY` | credenciais AWS |
| `AWS_S3_BUCKET` | bucket de mídia |

## API

Base: `/api`. Auth via header `Authorization: Bearer <jwt>`.

| Grupo | Rotas principais |
|---|---|
| `/api/user` | `POST /create`, `POST /signin`, `GET /get/{id}/{ownerId}`, `GET /get/recent`, `DELETE /delete/{ownerId}` |
| `/api/userprofile` | `GET /get/{username}`, `PUT /update/{userId}/{ownerId}`, `POST /update/pfp/{userId}` |
| `/api/community` | `POST /create/{userId}`, `POST /update/{userId}`, `POST /join/{userId}`, `GET /get/community/{community}`, `GET /get/all/community`, `DELETE /delete/{communityName}/{ownerId}` |
| `/api/post` | `POST /create/{userId}` (multipart), `GET /get/recent`, `GET /get/all/{maxPerPage}/{userId}`, `GET /search/{maxPerPage}/{userId}/{searchQuery}`, `PUT /update/{postId}/{ownerId}`, `DELETE /delete/{postId}/{ownerId}` |
| `/api/comment` | `POST /create/{userId}`, `GET /get/all/{community}`, `DELETE /delete/{commentId}/{ownerId}` |

Listagens de post são paginadas via parâmetro `maxPerPage`.

## Domínio

- **User** — credenciais, auth via JWT
- **UserProfile** — perfil público, foto (S3)
- **Community** — comunidade temática, dono e participantes
- **UserCommunity** — pivot de participação (join)
- **Post** — publicação com mídia opcional em S3, pertence a uma community
- **Comment** — comentário em post
- **UserLike** — curtida de usuário em post

## Decisões técnicas

Contexto e trade-offs das principais escolhas (JWT, upload para S3, delete transacional, paginação): **[devdiegofernandes.com/projects/neosrate](https://devdiegofernandes.com/projects/neosrate)**

## Licença

MIT
