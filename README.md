# Projeto: Curso Udemy - Arquitetura de Microsserviços: Padrão Transactional Outbox

Repositório contendo o projeto desenvolvido do curso Arquitetura de Microsserviços: Padrão Transactional Outbox, ministrado por mim para a plataforma
**Udemy**.

Para acessar o curso na plataforma, basta acessar esta URL: https://www.udemy.com/course/arquitetura-de-microsservicos-padrao-transactional-outbox/ (ainda não publicado)

![Curso](Conte%C3%BAdos/Imagem%20Curso.png)


### Sumário:

* [Tecnologias](#tecnologias)
* [Ferramentas utilizadas](#ferramentas-utilizadas)
* [Arquitetura Proposta](#arquitetura-proposta)
* [Execução do projeto](#execu%C3%A7%C3%A3o-do-projeto)
  * [01 - Execução geral via docker-compose](#01---execu%C3%A7%C3%A3o-geral-via-docker-compose)
  * [02 - Executando os serviços de bancos de dados e Message Broker](#02---executando-os-servi%C3%A7os-de-bancos-de-dados-e-message-broker)
  * [03 - Executando manualmente via CLI](#03---executando-manualmente-via-cli)
* [Acessando a aplicação](#acessando-a-aplica%C3%A7%C3%A3o)
* [Acessando tópicos com Redpanda Console](#acessando-t%C3%B3picos-com-redpanda-console)
* [Dados da API](#dados-da-api)
  * [Criar usuário](#criar-usuário)
  * [Atualizar usuário](#atualizar-usuário)
  * [Remover usuário](#remover-usuário)
  * [Estrutura de payloads do Kafka](#estrutura-de-payloads-do-kafka)
  * [Acesso ao MongoDB](#acesso-ao-mongodb)

## Tecnologias

[Voltar ao início](#sum%C3%A1rio)

* **Java 17**
* **Spring Boot 3**
* **Apache Kafka**
* **API REST**
* **PostgreSQL**
* **MongoDB**
* **Docker**
* **docker-compose**
* **Redpanda Console**

# Ferramentas utilizadas

[Voltar ao início](#sum%C3%A1rio)

* **IntelliJ IDEA Community Edition**
* **Docker**
* **Gradle**

# Arquitetura Proposta

[Voltar ao início](#sum%C3%A1rio)

No curso, desenvolveremos a seguinte aquitetura:

![Arquitetura](Conte%C3%BAdos/Arquitetura%20Proposta.png)

Em nossa arquitetura, teremos 2 serviços:

* **User-Service**: microsserviço responsável por manipular os dados do usuário. É nele que iremos criar, atualizar, buscar e remover usuários.
  * Este serviço terá **2 componentes**:
      * **Outbox Table Listener**: ficará ouvindo a tabela Outbox toda vez que houver um **INSERT**, para realizar o primeiro enviar ao Kafka.
      * **Outbox Scheduler**: agendador que irá eventualmente consultar os Outbox enviados com sucesso para remover da tabela, e buscará os Outbox pendentes para tentar reenviar ao Kafka.
* **Log-Service**: microsserviço responsável por receber qualquer evento do Kafka, registrar e possibilitar a busca por vários filtros.

Todos os serviços da arquitetura irão subir através do arquivo **docker-compose.yml**.

## Execução do projeto

[Voltar ao início](#sum%C3%A1rio)

Há várias maneiras de executar os projetos:

1. Executando tudo via `docker-compose`.
2. Executando apenas os serviços de bancos de dados e message broker (Kafka) separadamente.
3. Executando as aplicações manualmente via CLI (`java -jar` ou `gradle bootRun` ou via IntelliJ).

Para rodar as aplicações, será necessário ter instalado:

* **Docker**
* **Java 17**
* **Gradle 7.6 ou superior**

### 01 - Execução geral via docker-compose

[Voltar ao nível anterior](#execu%C3%A7%C3%A3o-do-projeto)

Basta executar o comando no diretório raiz do repositório:

`docker-compose up --build -d`

**Obs.: para rodar tudo desta maneira, é necessário realizar o build das 5 aplicações, veja nos passos abaixo sobre como fazer isto.**

### 02 - Executando os serviços de bancos de dados e Message Broker

[Voltar ao nível anterior](#execu%C3%A7%C3%A3o-do-projeto)

Para que seja possível executar os serviços de bancos de dados e Message Broker, como MongoDB, PostgreSQL e Apache Kafka, basta ir no diretório raiz do repositório, onde encontra-se o arquivo `docker-compose.yml` e executar o comando:

`docker-compose up --build -d order-db kafka product-db payment-db inventory-db`

Como queremos rodar apenas os serviços de bancos de dados e Message Broker, é necessário informá-los no comando do `docker-compose`, caso contrário, as aplicações irão subir também.

Para parar todos os containers, basta rodar:

`docker-compose down`

Ou então:

`docker stop ($docker ps -aq)`
`docker container prune -f`

### 03 - Executando manualmente via CLI

[Voltar ao nível anterior](#execu%C3%A7%C3%A3o-do-projeto)

Antes da execução do projeto, realize o `build` da aplicação indo no diretório raiz e executando o comando:

`gradle build -x test`

Para executar os projetos com Gradle, basta entrar no diretório raiz de cada projeto, e executar o comando:

`gradle bootRun`

Ou então, entrar no diretório: `build/libs` e executar o comando:

`java -jar nome_do_jar.jar`

## Acessando a aplicação

[Voltar ao início](#sum%C3%A1rio)

Para acessar as aplicações e realizar um pedido, basta acessar a URL:

http://localhost:8080/swagger-ui.html

Você chegará nesta página:

![Swagger](Conte%C3%BAdos/Documentacao.png)

As aplicações executarão nas seguintes portas:

* User-Service: 8080
* Log-Service: 8081
* Apache Kafka: 9092
* Redpanda Console: 3000
* PostgreSQL (User-DB): 5432
* MongoDB (Log-DB): 27017

## Acessando tópicos com Redpanda Console

[Voltar ao início](#sum%C3%A1rio)

Para acessar o Redpanda Console e visualizar tópicos e publicar eventos, basta acessar:

http://localhost:3000

Você chegará nesta página:

![Redpanda](Conte%C3%BAdos/Redpanda%20Kafka.png)

## Dados da API

[Voltar ao início](#sum%C3%A1rio)

É necessário conhecer os payloads de envio à API de usuários para simular o Outbox.

### Criar usuário

[Voltar ao nível anterior](#dados-da-api)

Para criar um usuário:

**POST** http://localhost:8080/api/user

**Input**:

```json
{
  "username": "test.user",
  "fullName": "Test User",
  "document": "11122233344",
  "email": "test@test.com",
  "birthday": "1998-03-31",
  "status": "ACTIVE"
}
```

**Output**:

```json
{
  "id": 1,
  "username": "test.user",
  "fullName": "Test User",
  "document": "11122233344",
  "email": "test@test.com",
  "birthday": "1998-03-31",
  "status": "ACTIVE",
  "createdAt": "2023-10-18T17:32:58.853838",
  "updatedAt": "2023-10-18T17:32:58.853838"
}
```

### Atualizar usuário

[Voltar ao nível anterior](#dados-da-api)

Para atualizar um usuário:

**PUT** http://localhost:8080/api/user/1

**Input**:

```json
{
  "username": "test.user",
  "fullName": "Test User",
  "document": "11122233344",
  "email": "test@test.com",
  "birthday": "1998-03-31",
  "status": "INACTIVE"
}
```

**Output**:

```json
{
  "id": 1,
  "username": "test.user",
  "fullName": "Test User",
  "document": "11122233344",
  "email": "test@test.com",
  "birthday": "1998-03-31",
  "status": "INACTIVE",
  "createdAt": "2023-10-18T17:32:58.853838",
  "updatedAt": "2023-10-18T17:34:28.61146"
}
```

### Remover usuário:

[Voltar ao nível anterior](#dados-da-api)

Para remover um usuário:

**DELETE** http://localhost:8080/api/user/1

A resposta serão os dados do usuário que foi removido.

**Output**:

```json
{
  "id": 1,
  "username": "test.user",
  "fullName": "Test User",
  "document": "11122233344",
  "email": "test@test.com",
  "birthday": "1998-03-31",
  "status": "INACTIVE",
  "createdAt": "2023-10-18T17:32:58.853838",
  "updatedAt": "2023-10-18T17:34:28.61146"
}
```

### Estrutura de payloads do Kafka

[Voltar ao nível anterior](#dados-da-api)

A estrutura do payload a ser enviada ao Kafka é:

```json
{
    "user": {
        "id": 1,
        "username": "test.user",
        "fullName": "Test User",
        "document": "11122233344",
        "email": "test@test.com",
        "birthday": "1998-03-31",
        "status": "ACTIVE",
        "createdAt": "2023-10-18T17:32:58.853838",
        "updatedAt": "2023-10-18T17:32:58.853838"
    },
    "action": "CREATE_USER",
    "httpMethod": "POST",
    "url": "http://localhost:8080/api/user",
    "createdAt": "2023-10-18T17:32:58.9035381"
}
```

### Acesso ao MongoDB

[Voltar ao início](#sum%C3%A1rio)

Para conectar-se ao MongoDB via linha de comando (cli) diretamente do docker-compose, basta executar o comando abaixo:

**docker exec -it order-db mongosh "mongodb://admin:123456@localhost:27017"**

Para listar os bancos de dados existentes:

**show dbs**

Para selecionar um banco de dados:

**use admin**

Para visualizar as collections do banco:

**show collections**

Para realizar queries e validar se os dados existem:

**db.log.find()**

**db.log.find(id=ObjectId("65006786d715e21bd38d1634"))**

**db.log.find({ "user.status": "ACTIVE"})**

## Autor

### Victor Hugo Negrisoli
### Desenvolvedor de Software Back-End