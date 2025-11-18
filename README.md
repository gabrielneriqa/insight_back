# InsightTrack – Backend

API REST desenvolvida em **Spring Boot** para suportar a plataforma InsightTrack, responsável por:

- Autenticação de usuários (login + cadastro)
- Gestão de campanhas de marketing
- Registro de resultados de campanha
- Geração de relatórios consolidados
- Isolamento de dados por usuário (cada usuário vê apenas suas campanhas)

---

## 🧱 Tecnologias utilizadas

- **Java 17+**
- **Spring Boot 3**
- **Spring Web**
- **Spring Data JPA**
- **Spring Security** (para criptografia de senha / JWT)
- **H2 Database** (banco em memória para desenvolvimento)
- **Maven**

---

## 🧬 Arquitetura em camadas

O projeto segue a estrutura:

- `entity` – entidades JPA (mapeamento do banco)
- `repository` – interfaces `JpaRepository` para acesso aos dados
- `service` – regras de negócio e orquestração
- `controller` – endpoints REST (camada de API)
- `dto` – objetos de transferência de dados (request/response)

---

## 🗄️ Modelagem de dados

### `Usuario`

Representa um usuário do sistema.

Campos principais:

- `id : Long`
- `nome : String`
- `email : String` (único)
- `senha : String` (armazenada **criptografada** com BCrypt)

### `Campanha`

Campanha de marketing pertencente a um usuário.

Campos:

- `id : Long`
- `nome : String`
- `usuario : Usuario` (`@ManyToOne`)

Relação:

- Um `Usuario` pode ter **várias campanhas**

### `ResultadoCampanha`

Registro de resultado de uma campanha em uma determinada data.

Campos:

- `id : Long`
- `campanha : Campanha` (`@ManyToOne`)
- `alcance : Integer`
- `engajamento : Integer`
- `cliques : Integer`
- `leads : Integer`
- `data : LocalDate`

Relações:

- Uma `Campanha` pode ter **vários resultados**
- Resultados são usados para montar o **relatório consolidado** da campanha

---

## 👤 Usuários demo e dados iniciais

No `DemoApplication` existe um `CommandLineRunner` que cria **dois usuários de demonstração** e algumas campanhas com resultados, se ainda não existirem:

### Usuário 1 – Administrador

- E-mail: `admin@insighttrack.com`
- Senha: `123456`

Campanhas de exemplo:

- `Campanha Instagram - Admin`
- `Campanha Google Ads - Admin`

Com alguns resultados (alcance, engajamento, cliques, leads) distribuídos em datas recentes.

### Usuário 2 – Gestor de Marketing

- E-mail: `gestor@insighttrack.com`
- Senha: `123456`

Campanhas de exemplo:

- `Campanha Facebook - Gestor`
- `Campanha LinkedIn - Gestor`

Também com resultados de demonstração.

> Esses dados são criados apenas se não existirem usuários com esses e-mails no banco.

---

## 🔐 Autenticação e segurança

- Senhas são armazenadas de forma **criptografada** usando `PasswordEncoder` (BCrypt).
- No login, a senha informada é validada com `passwordEncoder.matches(...)`.
- Em caso de sucesso, é gerado um **token** (via `TokenService`) e retornado no `LoginResponseDTO`.

### Endpoints de autenticação

#### `POST /api/auth/login`

Request (JSON):

```json
{
  "email": "admin@insighttrack.com",
  "senha": "123456"
}
Response (200):

json
Copy code
{
  "token": "token_jwt_ou_similar",
  "usuarioId": 1,
  "nome": "Administrador"
}
Em caso de falha (usuário ou senha inválidos), retorna:

HTTP 401 Unauthorized

POST /api/auth/registrar
Cadastra um novo usuário.

Request (JSON):

json
Copy code
{
  "nome": "Novo Usuário",
  "email": "novo@teste.com",
  "senha": "123456"
}
Regras:

O e-mail deve ser único.

A senha é automaticamente criptografada antes de salvar.

Responses:

201 Created → usuário criado

400 Bad Request → se o e-mail já existir ("E-mail já cadastrado")

🎯 Endpoints de campanhas
Controller: CampanhaController
Base: /api/campanhas
@CrossOrigin("*") habilitado para permitir acesso pelo frontend.

Importante: todas as campanhas estão vinculadas a um usuarioId.
O frontend envia esse usuarioId no corpo ao criar campanhas e usa /usuario/{usuarioId} para listar.

POST /api/campanhas
Cria uma nova campanha para um usuário.

Request (JSON):

json
Copy code
{
  "nome": "Campanha Black Friday",
  "usuarioId": 1
}
Response (201/200):

json
Copy code
{
  "id": 3,
  "nome": "Campanha Black Friday",
  "usuario": {
    "id": 1,
    "nome": "Administrador",
    "email": "admin@insighttrack.com"
  }
}
GET /api/campanhas/{id}
Busca uma campanha pelo ID.

Response (200):

json
Copy code
{
  "id": 1,
  "nome": "Campanha Instagram - Admin",
  "usuario": {
    "id": 1,
    "nome": "Administrador",
    "email": "admin@insighttrack.com"
  }
}
Se não existir, é lançada uma exceção com mensagem “Campanha não encontrada”.

GET /api/campanhas/usuario/{usuarioId}
Lista todas as campanhas de um determinado usuário.

Exemplo:

http
Copy code
GET /api/campanhas/usuario/1
Response (200):

json
Copy code
[
  {
    "id": 1,
    "nome": "Campanha Instagram - Admin",
    "usuario": { "id": 1, "nome": "Administrador" }
  },
  {
    "id": 2,
    "nome": "Campanha Google Ads - Admin",
    "usuario": { "id": 1, "nome": "Administrador" }
  }
]
É esse endpoint que o frontend usa para montar a lista "Minhas Campanhas"
e o <select> da tela de cadastro de resultados.

DELETE /api/campanhas/{id}
Remove uma campanha específica e todos os seus resultados associados.

Fluxo:

Verifica se a campanha existe.

Usa ResultadoCampanhaRepository.deleteByCampanhaId(id) para apagar os resultados.

Depois apaga a campanha: campanhaRepositorio.deleteById(id).

Responses:

204 No Content ou 200 OK (dependendo da configuração)

404 / erro se a campanha não existir

📈 Endpoints de resultados e relatórios
Controller: ResultadoCampanhaController
Base: /api/resultados
@CrossOrigin("*") habilitado.

POST /api/resultados
Registra um novo resultado de campanha.

Request (JSON):

json
Copy code
{
  "campanhaId": 1,
  "alcance": 50000,
  "engajamento": 3200,
  "cliques": 1500,
  "leads": 450,
  "data": "2025-11-15"
}
A data deve estar no formato yyyy-MM-dd (LocalDate).

Response (200/201):

json
Copy code
{
  "id": 10,
  "campanha": {
    "id": 1,
    "nome": "Campanha Instagram - Admin"
  },
  "alcance": 50000,
  "engajamento": 3200,
  "cliques": 1500,
  "leads": 450,
  "data": "2025-11-15"
}
GET /api/resultados/relatorio/{campanhaId}
Gera um relatório consolidado dos últimos 30 dias de uma campanha:

Soma de alcance, engajamento, cliques e leads.

O próprio controller calcula:

java
Copy code
LocalDate fim = LocalDate.now();
LocalDate inicio = fim.minusDays(30);
Response (200):

json
Copy code
{
  "campanhaId": 1,
  "nomeCampanha": "Campanha Instagram - Admin",
  "totalAlcance": 80000,
  "totalEngajamento": 5200,
  "totalCliques": 2300,
  "totalLeads": 650,
  "dataInicio": "2025-10-19",
  "dataFim": "2025-11-18"
}
Esse endpoint é consumido pelo frontend para montar o gráfico de barras com Chart.js.

📦 Regras importantes de negócio
Isolamento por usuário:

Campanhas são sempre criadas vinculadas a um Usuario (usuarioId).

O frontend usa /api/campanhas/usuario/{usuarioId} para listar campanhas.

Na prática, cada usuário só visualiza suas próprias campanhas.

Senhas seguras:

Senhas nunca são armazenadas em texto puro.

É usado PasswordEncoder (BCrypt) para criptografar.

Exclusão de campanha:

Remove resultados antes de remover a campanha, evitando registros “órfãos”.

Relatório automático:

Sempre considera a janela de 30 dias retroativos a partir da data atual.

⚙️ Como rodar o backend
Pré-requisitos
Java 17+

Maven

Passos
Clonar o repositório ou abrir o projeto na IDE.

Conferir o application.properties (por exemplo, porta e configuração do H2).

No terminal, na pasta do projeto, executar:

bash
Copy code
mvn clean spring-boot:run
A aplicação sobe em:

text
Copy code
http://localhost:8080
(Opcional) Se o H2 Console estiver habilitado, pode ser acessado em:

text
Copy code
http://localhost:8080/h2-console
🧪 Testes via Postman
Sugestão de ordem de testes:

POST /api/auth/login com admin@insighttrack.com / 123456

GET /api/campanhas/usuario/1

POST /api/campanhas para criar novas campanhas

POST /api/resultados para registrar novos resultados

GET /api/resultados/relatorio/{campanhaId} para ver o relatório consolidado

DELETE /api/campanhas/{id} para remover uma campanha e seus resultados

Também é possível testar o cadastro de novos usuários com:

POST /api/auth/registrar

E então logar com essas credenciais para ver campanhas isoladas.
