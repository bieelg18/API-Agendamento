# API de Agendamento

API REST desenvolvida com Java e Spring Boot para gerenciamento de agendamentos de serviços em uma barbearia.

O sistema permite o cadastro de usuários, profissionais e serviços, além da criação e gerenciamento de agendamentos. A aplicação possui autenticação utilizando JWT, controle de acesso baseado em permissões e validações de regras de negócio para evitar conflitos de horários.

## Tecnologias utilizadas

- Java
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- PostgreSQL
- Flyway
- Hibernate
- MapStruct
- Lombok
- JUnit 5
- Mockito
- Maven
- Swagger / OpenAPI

## Funcionalidades

### Usuários

- Cadastro de usuários
- Autenticação utilizando JWT
- Edição dos dados cadastrais
- Listagem de usuários
- Busca de usuários por e-mail
- Alteração de permissões
- Exclusão de usuários

### Serviços

- Cadastro de serviços
- Listagem de serviços disponíveis
- Edição de serviços
- Exclusão de serviços
- Definição de preço e duração do serviço

### Agendamentos

- Criação de agendamentos
- Associação entre cliente, profissional e serviço
- Listagem de agendamentos do usuário autenticado
- Listagem administrativa de agendamentos
- Alteração do status de um agendamento
- Exclusão de agendamentos
- Consulta de agendamentos por status

## Regras de negócio

A API implementa validações para garantir a consistência dos agendamentos.

Entre as principais regras estão:

- Não é permitido criar agendamentos em datas ou horários passados
- O cliente do agendamento é identificado através do usuário autenticado
- Apenas usuários com a permissão adequada podem atuar como profissionais
- Um profissional não pode possuir dois agendamentos em horários conflitantes
- A duração do serviço é considerada no cálculo de conflitos de horário
- Um novo agendamento pode começar exatamente no horário em que o agendamento anterior termina
- Apenas o profissional responsável pode alterar determinados dados do agendamento
- Agendamentos concluídos possuem restrições para alterações posteriores

## Autenticação e autorização

A autenticação da aplicação é realizada utilizando Spring Security e JWT.

Após realizar o login, a API gera um token JWT que deve ser enviado nas requisições autenticadas:

```http
Authorization: Bearer <token>
```

O acesso aos endpoints é controlado de acordo com a permissão do usuário, permitindo separar operações destinadas a clientes, profissionais e usuários administrativos.

## Banco de dados

A aplicação utiliza PostgreSQL como banco de dados relacional.

O versionamento e a criação da estrutura do banco são realizados através do Flyway, permitindo que as alterações no schema sejam controladas por migrations.

As principais entidades da aplicação são:

```text
Usuario
   |
   | 1
   |
   | N
Agendamento
   |
   | N
   |
   | 1
Servico
```

Um agendamento possui referências para:

- Cliente
- Profissional
- Serviço

Cliente e profissional são representados pela entidade `Usuario`, sendo diferenciados através de suas permissões.

## Testes

Os serviços da aplicação possuem testes unitários desenvolvidos com JUnit 5 e Mockito.

Foram implementados testes para cenários de sucesso e de exceção, incluindo validações de regras de negócio e interações com repositories e mappers.

```text
AgendamentosServiceTest: 23 testes
UsuarioServiceTest:      13 testes
ServicosServiceTest:      7 testes

Total de testes unitários dos services: 43
```

Além dos testes unitários, o projeto possui o teste de carregamento do contexto do Spring Boot.

Para executar todos os testes:

```bash
./mvnw test
```

No Windows:

```powershell
.\mvnw test
```

Resultado atual:

```text
Tests run: 44, Failures: 0, Errors: 0, Skipped: 0
```

## Documentação da API

A documentação dos endpoints está disponível através do Swagger/OpenAPI.

Com a aplicação em execução, a interface do Swagger permite visualizar e testar os endpoints disponíveis, incluindo os parâmetros, corpos das requisições e respostas da API.

## Configuração

A aplicação utiliza variáveis de ambiente para informações sensíveis e configurações externas.

Exemplo:

```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USERNAME}
spring.datasource.password=${DATABASE_PASSWORD}
```

Também é necessária uma variável para o segredo utilizado na geração e validação dos tokens JWT:

```text
JWT_SECRET
```

Exemplo de configuração da conexão PostgreSQL:

```text
DATABASE_URL=jdbc:postgresql://localhost:5432/db_agendamentos
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=sua_senha
```

Por segurança, credenciais e chaves secretas não devem ser versionadas no repositório.

## Executando o projeto

Clone o repositório:

```bash
git clone https://github.com/bieelg18/API-Agendamento.git
```

Entre no diretório:

```bash
cd API-Agendamento
```

Configure as variáveis de ambiente necessárias e execute:

### Windows

```powershell
.\mvnw spring-boot:run
```

### Linux / macOS

```bash
./mvnw spring-boot:run
```

## Autor

Desenvolvido por Gabriel Alves.

GitHub: `bieelg18`