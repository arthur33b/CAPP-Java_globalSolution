# CAPP API - Sistema de Gestão de Aulas

API REST desenvolvida com Spring Boot para gerenciamento de aulas, professores, alunos e avaliações.

## 🚀 Tecnologias

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Security + JWT
- Oracle Database
- Maven
- Lombok
- Swagger/OpenAPI 3.0
- Bean Validation

## 📋 Pré-requisitos

- JDK 17 ou superior
- Maven 3.6+
- Oracle Database (XE ou superior)
- Git

## ⚙️ Configuração

### 1. Banco de Dados

Execute o script SQL localizado em `table.sql` para criar as tabelas necessárias:

```bash
sqlplus usuario/senha@localhost:1521/xe @table.sql
```

### 2. Configuração da Aplicação

Edite o arquivo `src/main/resources/application.properties` e configure as credenciais do banco:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:xe
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 3. Executar a Aplicação

```bash
mvn spring-boot:run
```

A API estará disponível em: `http://localhost:8080`

**Swagger UI**: `http://localhost:8080/swagger-ui.html`

## 🔐 Autenticação

A API utiliza **JWT (JSON Web Token)** para autenticação.

### Login

**Endpoint**: `POST /api/auth/login`

**Request Body**:
```json
{
  "email": "usuario@email.com",
  "senha": "senha123"
}
```

**Response**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "email": "usuario@email.com"
}
```

### Usando o Token

Adicione o token no header `Authorization` de todas as requisições:

```
Authorization: Bearer {seu-token-aqui}
```

**Exemplo com Axios**:
```javascript
api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
```

## 📚 Endpoints da API

### Áreas

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/areas` | Lista todas as áreas |
| GET | `/api/areas/{id}` | Busca área por ID |
| POST | `/api/areas` | Cria nova área |
| PUT | `/api/areas/{id}` | Atualiza área |
| DELETE | `/api/areas/{id}` | Remove área |

**Exemplo de Request (POST/PUT):**
```json
{
  "nome": "Tecnologia",
  "descricao": "Área de tecnologia da informação"
}
```

### Modalidades

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/modalidades` | Lista todas as modalidades |
| GET | `/api/modalidades/{id}` | Busca modalidade por ID |
| GET | `/api/modalidades/area/{areaId}` | Lista modalidades por área |
| POST | `/api/modalidades` | Cria nova modalidade |
| PUT | `/api/modalidades/{id}` | Atualiza modalidade |
| DELETE | `/api/modalidades/{id}` | Remove modalidade |

**Exemplo de Request (POST/PUT):**
```json
{
  "nome": "Java Avançado",
  "descricao": "Curso de Java para níveis avançados",
  "areaId": 1
}
```

### Professores

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/professores` | Lista todos os professores |
| GET | `/api/professores/{id}` | Busca professor por ID |
| POST | `/api/professores` | Cria novo professor |
| PUT | `/api/professores/{id}` | Atualiza professor |
| DELETE | `/api/professores/{id}` | Remove professor |

**Exemplo de Request (POST/PUT):**
```json
{
  "nome": "João Silva",
  "email": "joao.silva@email.com",
  "senha": "senha123",
  "telefone": "(11) 98765-4321",
  "bio": "Professor com 10 anos de experiência",
  "experiencia": "Especialista em Java e Spring"
}
```

### Alunos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/alunos` | Lista todos os alunos |
| GET | `/api/alunos/{id}` | Busca aluno por ID |
| POST | `/api/alunos` | Cria novo aluno |
| PUT | `/api/alunos/{id}` | Atualiza aluno |
| DELETE | `/api/alunos/{id}` | Remove aluno |

**Exemplo de Request (POST/PUT):**
```json
{
  "nome": "Maria Santos",
  "email": "maria.santos@email.com",
  "senha": "senha456",
  "telefone": "(11) 91234-5678"
}
```

### Aulas

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/aulas` | Lista todas as aulas |
| GET | `/api/aulas/{id}` | Busca aula por ID |
| GET | `/api/aulas/aluno/{alunoId}` | Lista aulas por aluno |
| GET | `/api/aulas/professor/{professorId}` | Lista aulas por professor |
| GET | `/api/aulas/modalidade/{modalidadeId}` | Lista aulas por modalidade |
| GET | `/api/aulas/status/{status}` | Lista aulas por status |
| POST | `/api/aulas` | Cria nova aula |
| PUT | `/api/aulas/{id}` | Atualiza aula |
| DELETE | `/api/aulas/{id}` | Remove aula |

**Exemplo de Request (POST/PUT):**
```json
{
  "status": "Agendada",
  "link": "https://meet.google.com/abc-defg-hij",
  "preco": 150.00,
  "quantidadeHoras": 2.5,
  "modalidadeId": 1,
  "professorId": 1,
  "alunoId": 1
}
```

### Avaliações

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET | `/api/avaliacoes` | Lista todas as avaliações |
| GET | `/api/avaliacoes/{id}` | Busca avaliação por ID |
| GET | `/api/avaliacoes/aula/{aulaId}` | Lista avaliações por aula |
| POST | `/api/avaliacoes` | Cria nova avaliação |
| PUT | `/api/avaliacoes/{id}` | Atualiza avaliação |
| DELETE | `/api/avaliacoes/{id}` | Remove avaliação |

**Exemplo de Request (POST/PUT):**
```json
{
  "nota": 9,
  "comentario": "Excelente aula, professor muito didático",
  "aulaId": 1
}
```

## 🔍 Validações

A API possui validações automáticas nos seguintes campos:

- **Email**: Formato válido de e-mail
- **Campos obrigatórios**: Nome, email, senha, status, preço
- **Nota**: Valor entre 0 e 10
- **Preço**: Valor positivo
- **Tamanhos máximos**: Respeitam os limites do banco de dados

## 📄 Paginação e Ordenação

Todos os endpoints GET (listagem) suportam paginação, ordenação e filtros via parâmetros de URL:

**Parâmetros**:
- `page`: Número da página (inicia em 0)
- `size`: Tamanho da página (padrão: 10, máximo: 100)
- `sort`: Campo e direção de ordenação (ex: `nome,asc` ou `id,desc`)

**Exemplo**:
```
GET /api/areas?page=0&size=20&sort=nome,asc
GET /api/aulas?page=1&size=10&sort=preco,desc
```

**Response**:
```json
{
  "content": [...],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 50,
  "totalPages": 5,
  "last": false
}
```

## 📖 Documentação Swagger

Acesse a documentação interativa da API:

**Swagger UI**: `http://localhost:8080/swagger-ui.html`

**OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

Na interface Swagger você pode:
- Ver todos os endpoints disponíveis
- Testar as requisições diretamente
- Ver os schemas de request/response
- Autenticar com JWT

## 📝 Estrutura do Projeto

```
src/
├── main/
│   ├── java/org/example/
│   │   ├── controller/     # Controllers REST
│   │   ├── dto/            # DTOs de Request e Response
│   │   ├── entity/         # Entidades JPA
│   │   ├── exception/      # Tratamento de exceções
│   │   ├── repository/     # Repositories JPA
│   │   ├── service/        # Camada de serviço
│   │   └── CappApiApplication.java
│   └── resources/
│       └── application.properties
└── test/
```

## 🛠️ Build e Deploy

### Compilar o projeto

```bash
mvn clean package
```

### Executar o JAR

```bash
java -jar target/CAPP-API-1.0-SNAPSHOT.jar
```

## � Deploy

Para instruções detalhadas de deploy em diferentes plataformas de nuvem, consulte o arquivo [DEPLOY.md](DEPLOY.md).

**Plataformas suportadas**:
- Oracle Cloud
- AWS (Elastic Beanstalk, ECS, EC2)
- Azure App Service
- Heroku
- Google Cloud Platform (Cloud Run)

## ✅ Requisitos Atendidos

✅ **API Rest** seguindo boas práticas da arquitetura  
✅ **Persistência em Banco de Dados Relacional** usando Spring Data JPA  
✅ **Mapeamento de relacionamentos** entre entidades  
✅ **Validação com Bean Validation**  
✅ **Paginação, ordenação e filtros**  
✅ **Documentação da API com Swagger**  
✅ **Autenticação com JWT**  
✅ **Preparado para Deploy em nuvem**

## 🔒 Segurança

- Autenticação JWT com token bearer
- Senhas não são expostas nas respostas
- CORS configurado
- Endpoints públicos apenas para login e documentação
- Tokens com expiração configurável (padrão: 24h)

## �📄 Licença

Este projeto foi desenvolvido para fins educacionais.

## 👥 Contato

Para dúvidas ou sugestões, entre em contato através do repositório.
