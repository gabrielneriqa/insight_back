# 📊 API de Campanhas — InsightTrack

Este projeto é uma API REST desenvolvida em **Spring Boot** para gerenciar campanhas e seus resultados, permitindo:

- Criar campanhas
- Inserir resultados associados a uma campanha
- Gerar relatório consolidado dos últimos 30 dias

---

# 🔧 Tecnologias utilizadas

- Java 17+
- Spring Boot
- Spring Data JPA
- H2 Database (em memória)
- Maven

---

# 🚀 Como rodar o projeto
✔️ 1. Requisitos

Antes de iniciar, instale:

Java 17 ou superior

Maven 3.8+

Uma IDE (opcional): IntelliJ, Eclipse ou VS Code

✔️ 2. Clonar o repositório

Se o projeto estiver no GitHub:

git clone https://github.com/gabrielneriqa/insight_back

Entre na pasta:

cd nome-do-projeto

✔️ 3. Rodar o backend

Execute:

mvn spring-boot:run


Se tudo estiver correto, você verá no console:

Tomcat started on port(s): 8080
Started DemoApplication


A API estará disponível em:

http://localhost:8080

✔️ 4. Acessar o banco H2 (opcional)

Com o projeto rodando, abra no navegador:

http://localhost:8080/h2-console


Use estas credenciais:

Campo	Valor
JDBC URL	jdbc:h2:mem:insighttrackdb
Username	sa
Password	(vazio)
✔️ 5. Encerrar o servidor

No terminal onde o projeto está rodando, pressione:

CTRL + C
---
# 🧪 Como testar a API (via Postman)

A API possui três operações principais:

Criar campanhas

Cadastrar resultados de campanhas

Gerar relatórios consolidados

A seguir estão os passos para testar cada funcionalidade no Postman.

✔️ 1. Criar uma Campanha
Método: POST
URL:
http://localhost:8080/api/campanhas

Body → raw → JSON
{
"nome": "Campanha Black Friday"
}

Resposta esperada:
{
"id": 1,
"nome": "Campanha Black Friday"
}


Guarde o id da campanha criada.
Ele será usado para associar os resultados.

✔️ 2. Cadastrar resultados de campanha
Método: POST
URL:
http://localhost:8080/api/resultados

Body → raw → JSON
{
"campanhaId": 1,
"alcance": 50000,
"engajamento": 3200,
"cliques": 1500,
"leads": 450,
"data": "2025-11-15"
}

Resposta esperada:
{
"id": 1,
"campanha": {
"id": 1,
"nome": "Campanha Black Friday"
},
"alcance": 50000,
"engajamento": 3200,
"cliques": 1500,
"leads": 450,
"data": "2025-11-15"
}


➡️ Você pode cadastrar quantos resultados quiser para o mesmo campanhaId.

✔️ 3. Gerar relatório da campanha (últimos 30 dias)
Método: GET
URL:
http://localhost:8080/api/resultados/relatorio/1


➡️ Substitua 1 pelo ID da campanha que deseja consultar.

Resposta esperada:
{
"nomeCampanha": "Campanha Black Friday",
"totalAlcance": 50000,
"totalEngajamento": 3200,
"totalCliques": 1500,
"totalLeads": 450
}


Se você tiver cadastrado vários resultados diferentes, o sistema somará todos e gerará o total consolidado.

🧪 Exemplos de testes mais completos

Para testar um relatório real, você pode inserir múltiplos resultados, como:

{
"campanhaId": 1,
"alcance": 30000,
"engajamento": 2000,
"cliques": 900,
"leads": 200,
"data": "2025-11-01"
}

{
"campanhaId": 1,
"alcance": 40000,
"engajamento": 3500,
"cliques": 1800,
"leads": 300,
"data": "2025-11-10"
}

{
"campanhaId": 1,
"alcance": 25000,
"engajamento": 1500,
"cliques": 700,
"leads": 160,
"data": "2025-11-20"
}

Relatório consolidado esperado:
{
"nomeCampanha": "Campanha Black Friday",
"totalAlcance": 95000,
"totalEngajamento": 7000,
"totalCliques": 3400,
"totalLeads": 660
}

⚠️ Erros comuns no Postman
❌ 404 — Endpoint não encontrado

Verifique se está usando as URLs corretas:

/api/campanhas

/api/resultados

/api/resultados/relatorio/{id}

❌ 400 — JSON inválido

Campos faltando

Data com formato errado (YYYY-MM-DD)

❌ 500 — Campanha não encontrada

O campanhaId informado não existe

Ou você tentou gerar relatório de uma campanha inexistente
