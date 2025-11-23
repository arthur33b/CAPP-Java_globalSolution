# CAPP API - Documentação do Desafio

## 📋 Sobre o Projeto

O **CAPP (Conectando Alunos, Professores e Profissionais)** é uma solução tecnológica inovadora que visa democratizar o acesso à educação e criar oportunidades mais justas, inclusivas e sustentáveis através da conexão entre alunos e professores para aulas personalizadas.

## 🎯 Proposta de Solução

### Problema Identificado
- Dificuldade de alunos em encontrar professores qualificados para aulas particulares
- Falta de plataforma centralizada para gerenciamento de aulas
- Ausência de sistema de avaliação e feedback estruturado
- Dificuldade de professores em divulgar seus serviços

### Solução Proposta
Uma plataforma digital que:
- **Conecta** alunos e professores de diversas áreas de conhecimento
- **Facilita** o agendamento e gerenciamento de aulas online/presenciais
- **Promove** transparência através de sistema de avaliações
- **Organiza** conteúdos por áreas e modalidades
- **Possibilita** pagamento e acompanhamento de histórico

## 🏗️ Arquitetura da Solução

### Componentes Principais

```
┌─────────────────┐
│   Front-End     │
│   (React/Vue)   │
└────────┬────────┘
         │
         │ HTTPS/REST
         │
┌────────▼────────┐
│   CAPP API      │
│  (Spring Boot)  │
└────────┬────────┘
         │
         │ JDBC
         │
┌────────▼────────┐
│ Oracle Database │
└─────────────────┘
```

### Tecnologias Utilizadas

**Backend**:
- ✅ Spring Boot 3.2.0
- ✅ Spring Data JPA
- ✅ Spring Security + JWT
- ✅ Bean Validation
- ✅ Swagger/OpenAPI

**Banco de Dados**:
- ✅ Oracle Database
- ✅ Relacionamentos complexos (1:N, N:1)
- ✅ Constraints e integridade referencial

**DevOps**:
- ✅ Docker
- ✅ Maven
- ✅ Preparado para deploy em múltiplas clouds

## ✅ Requisitos Técnicos Atendidos

### 1. API Rest - Boas Práticas ✅

- **REST Level 2** (Richardson Maturity Model)
- Uso correto de verbos HTTP (GET, POST, PUT, DELETE)
- Códigos de status HTTP apropriados
- URIs semânticas e consistentes
- Separação de concerns (Controller → Service → Repository)
- DTOs para Request e Response
- Tratamento global de exceções

**Exemplo de estrutura**:
```
/api/areas          → Gerenciamento de áreas
/api/modalidades    → Gerenciamento de modalidades
/api/professores    → Gerenciamento de professores
/api/alunos         → Gerenciamento de alunos
/api/aulas          → Gerenciamento de aulas
/api/avaliacoes     → Gerenciamento de avaliações
/api/auth           → Autenticação
```

### 2. Persistência com Spring Data JPA ✅

- Repositories utilizando `JpaRepository`
- Queries personalizadas quando necessário
- Transações gerenciadas com `@Transactional`
- Pool de conexões configurado
- Dialect específico para Oracle

**Exemplo**:
```java
@Repository
public interface AulaRepository extends JpaRepository<Aula, Integer> {
    List<Aula> findByAlunoId(Integer alunoId);
    List<Aula> findByProfessorId(Integer professorId);
    List<Aula> findByStatus(String status);
}
```

### 3. Mapeamento de Relacionamentos ✅

**Relacionamentos implementados**:

```
Area (1) ─────< (N) Modalidade
Modalidade (1) ─────< (N) Aula
Professor (1) ─────< (N) Aula
Aluno (1) ─────< (N) Aula
Aula (1) ─────< (N) Avaliacao
```

**Tipos de relacionamentos**:
- `@OneToMany` / `@ManyToOne`
- `@JoinColumn` para foreign keys
- Cascade operations configurados
- Lazy/Eager loading otimizado

### 4. Validação com Bean Validation ✅

**Validações implementadas**:

```java
@NotBlank(message = "Nome é obrigatório")
@Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
private String nome;

@Email(message = "Email inválido")
private String email;

@Min(value = 0, message = "Nota mínima é 0")
@Max(value = 10, message = "Nota máxima é 10")
private Integer nota;

@Positive(message = "Preço deve ser positivo")
private BigDecimal preco;
```

**Validações customizadas**:
- Formatos de email
- Tamanhos máximos/mínimos
- Valores numéricos dentro de ranges
- Campos obrigatórios

### 5. Paginação, Ordenação e Filtros ✅

**Paginação**:
```java
@GetMapping
public ResponseEntity<Page<AreaResponse>> findAll(
    @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
    return ResponseEntity.ok(areaService.findAll(pageable));
}
```

**Uso**:
```
GET /api/areas?page=0&size=20&sort=nome,asc
GET /api/aulas?page=1&size=10&sort=preco,desc
```

**Filtros customizados**:
- Por área
- Por professor
- Por aluno
- Por status
- Por modalidade

### 6. Documentação com Swagger ✅

**Swagger UI**: `http://localhost:8080/swagger-ui.html`

**Recursos documentados**:
- Todos os endpoints
- Schemas de Request/Response
- Códigos de resposta
- Exemplos de uso
- Autenticação JWT integrada

**Configuração**:
```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("CAPP API")
                .version("1.0")
                .description("API para gestão de aulas"))
            .addSecurityItem(new SecurityRequirement()
                .addList("bearerAuth"));
    }
}
```

### 7. Autenticação com JWT ✅

**Fluxo de Autenticação**:

1. **Login** → `POST /api/auth/login`
```json
{
  "email": "usuario@email.com",
  "senha": "senha123"
}
```

2. **Resposta** → Token JWT
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tipo": "Bearer",
  "email": "usuario@email.com"
}
```

3. **Uso** → Header Authorization
```
Authorization: Bearer {token}
```

**Implementação**:
- JWT Service para geração e validação
- Filter customizado (JwtAuthenticationFilter)
- Spring Security configurado
- Tokens com expiração (24h padrão)
- Senhas não são expostas nas responses

**Segurança**:
- Endpoints públicos: `/api/auth/**`, `/swagger-ui/**`
- Endpoints protegidos: Todos os demais
- CORS configurado
- CSRF desabilitado (stateless)

### 8. Deploy em Nuvem ✅

**Opções suportadas**:

1. **Oracle Cloud**
   - Autonomous Database
   - Container Instances
   - Compute Instances

2. **AWS**
   - Elastic Beanstalk
   - ECS (containerizado)
   - EC2

3. **Azure**
   - App Service
   - Container Instances

4. **Heroku**
   - Deploy direto via Git

5. **Google Cloud Platform**
   - Cloud Run
   - App Engine

**Recursos**:
- ✅ Dockerfile pronto
- ✅ Profile de produção configurado
- ✅ Variáveis de ambiente
- ✅ Guia completo em `DEPLOY.md`

## 📊 Modelo de Dados

### Entidades

| Entidade | Descrição | Campos Principais |
|----------|-----------|-------------------|
| **Area** | Áreas de conhecimento | nome, descrição |
| **Modalidade** | Tipos de curso | nome, descrição, área |
| **Professor** | Cadastro de professores | nome, email, bio, experiência |
| **Aluno** | Cadastro de alunos | nome, email, telefone |
| **Aula** | Aulas agendadas | status, link, preço, modalidade, professor, aluno |
| **Avaliacao** | Avaliações de aulas | nota, comentário, aula |

### Diagrama ER

```
┌─────────┐
│  Area   │
└────┬────┘
     │ 1:N
┌────▼──────────┐
│  Modalidade   │
└────┬──────────┘
     │ 1:N
┌────▼────┐     ┌───────────┐     ┌─────────┐
│  Aula   │─────│ Professor │     │  Aluno  │
└────┬────┘ N:1 └───────────┘     └────┬────┘
     │                                  │
     │ 1:N                           N:1│
┌────▼────────┐                        │
│  Avaliacao  │                        │
└─────────────┘                        │
                  ────────────────────┘
```

## 🎓 Impacto Social

### Como a Solução Melhora a Vida das Pessoas

1. **Acessibilidade**: Facilita acesso a professores qualificados independente da localização
2. **Inclusão**: Democratiza o acesso à educação personalizada
3. **Sustentabilidade**: Reduz custos com deslocamento (aulas online)
4. **Oportunidades**: Cria oportunidades de renda para professores
5. **Qualidade**: Sistema de avaliação garante qualidade do ensino

### Diferenciais

- ✅ Sistema completo de gestão
- ✅ Avaliações e feedback estruturado
- ✅ Organização por áreas e modalidades
- ✅ Histórico de aulas
- ✅ Controle de preços e pagamentos
- ✅ Interface documentada (Swagger)
- ✅ Segurança (JWT)
- ✅ Escalável (Cloud-ready)

## 📈 Possíveis Expansões Futuras

1. **Sistema de Pagamento**: Integração com gateways
2. **Chat em Tempo Real**: WebSocket para comunicação
3. **Calendário Inteligente**: Sugestão de horários
4. **IA para Recomendações**: Machine Learning para sugestões
5. **Gamificação**: Sistema de pontos e badges
6. **Relatórios**: Dashboard com métricas
7. **Notificações**: Email/SMS para lembretes

## 🏆 Conclusão

A **CAPP API** atende a todos os requisitos técnicos do desafio, implementando uma solução robusta, escalável e moderna para o problema de conectar alunos e professores. A aplicação utiliza as melhores práticas de desenvolvimento, incluindo arquitetura em camadas, segurança, documentação e preparação para deploy em ambiente de produção.

A API está pronta para ser consumida por um front-end e pode ser facilmente expandida com novas funcionalidades, demonstrando preparação para um cenário real de mercado.
