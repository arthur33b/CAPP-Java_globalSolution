# 🔐 Autenticação e Senhas - Guia Completo

## ⚠️ Como Funciona a Autenticação

A API usa **JWT (JSON Web Token)** com **BCrypt** para criptografia de senhas.

### 🔑 Fluxo de Autenticação

```
1. Cadastro → Senha criptografada com BCrypt → Salva no banco
2. Login → Compara senha informada com hash no banco
3. Se válido → Gera token JWT
4. Requisições → Usa token JWT no header Authorization
```

## 📝 Como Cadastrar Usuários

### **Opção 1: Via API (Recomendado)**

As senhas são **automaticamente criptografadas** ao cadastrar via API:

```bash
# Cadastrar Professor
POST /api/professores
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "senha": "senha123",        # ← Será criptografada automaticamente
  "telefone": "(11) 98765-4321",
  "bio": "Professor experiente",
  "experiencia": "10 anos"
}

# Cadastrar Aluno
POST /api/alunos
{
  "nome": "Maria Santos",
  "email": "maria@email.com",
  "senha": "senha456",        # ← Será criptografada automaticamente
  "telefone": "(11) 91234-5678"
}
```

### **Opção 2: Inserir Direto no Banco (Desenvolvimento)**

Se inserir direto no SQL, precisa criptografar a senha primeiro:

#### Gerar Hash BCrypt:

**Online**: https://bcrypt-generator.com/
- Rounds: 10
- Exemplo: `senha123` → `$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy`

**SQL com senha criptografada**:
```sql
INSERT INTO TB_CAPP_PROFESSOR (
    ID_professor, nome_professor, email_professor, 
    senha_professor, telefone_professor, data_cadastro
) VALUES (
    1, 
    'João Silva', 
    'joao@email.com', 
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- senha123 criptografada
    '(11) 98765-4321',
    SYSDATE
);

INSERT INTO TB_COURAPP_ALUNO (
    ID_aluno, nome_aluno, email_aluno, 
    senha_aluno, telefone_aluno, data_cadastro
) VALUES (
    1,
    'Maria Santos',
    'maria@email.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- senha123 criptografada
    '(11) 91234-5678',
    SYSDATE
);
```

## 🔓 Como Fazer Login

```bash
POST /api/auth/login
Content-Type: application/json

{
  "email": "joao@email.com",
  "senha": "senha123"        # ← Senha em texto puro (não criptografada)
}
```

**Resposta**:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2FvQGVtYWlsLmNvbSIsImlhdCI6MTYxNjIzOTAyMiwiZXhwIjoxNjE2MzI1NDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
  "tipo": "Bearer",
  "email": "joao@email.com"
}
```

## 🚀 Como Usar o Token

### Com Axios (JavaScript):

```javascript
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api'
});

// 1. Fazer Login
const login = async () => {
  const response = await api.post('/auth/login', {
    email: 'joao@email.com',
    senha: 'senha123'
  });
  
  const token = response.data.token;
  
  // Salvar token (localStorage, sessionStorage, etc)
  localStorage.setItem('token', token);
  
  // Configurar header padrão
  api.defaults.headers.common['Authorization'] = `Bearer ${token}`;
};

// 2. Usar em requisições
const listarAreas = async () => {
  const response = await api.get('/areas');
  return response.data;
};
```

### Com cURL:

```bash
# Fazer login e pegar o token
TOKEN=$(curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"joao@email.com","senha":"senha123"}' \
  | jq -r '.token')

# Usar o token nas requisições
curl -X GET http://localhost:8080/api/areas \
  -H "Authorization: Bearer $TOKEN"
```

### Com Postman/Insomnia:

1. Fazer requisição de login
2. Copiar o `token` da resposta
3. Na aba **Authorization**:
   - Type: `Bearer Token`
   - Token: `{colar-token-aqui}`

## 🔒 Endpoints Públicos vs Protegidos

### ✅ Públicos (Não precisam de token):
- `POST /api/auth/login` - Login
- `GET /swagger-ui.html` - Documentação Swagger
- `GET /v3/api-docs` - OpenAPI JSON

### 🔐 Protegidos (Precisam de token):
- Todos os demais endpoints da API
- `/api/areas/**`
- `/api/modalidades/**`
- `/api/professores/**`
- `/api/alunos/**`
- `/api/aulas/**`
- `/api/avaliacoes/**`

## ⚙️ Configurações de Segurança

### Token JWT:
- **Expiração**: 24 horas (configurável em `application.properties`)
- **Algoritmo**: HS256
- **Secret**: Configurável via `jwt.secret`

### BCrypt:
- **Rounds**: 10 (padrão)
- **Algoritmo**: BCrypt

## 🧪 Testando no Swagger

1. Acesse: `http://localhost:8080/swagger-ui.html`
2. Clique no endpoint `POST /api/auth/login`
3. Clique em **Try it out**
4. Preencha:
   ```json
   {
     "email": "joao@email.com",
     "senha": "senha123"
   }
   ```
5. Clique em **Execute**
6. Copie o `token` da resposta
7. Clique no botão **Authorize** (cadeado) no topo da página
8. Cole o token (sem "Bearer ", só o token)
9. Clique em **Authorize**
10. Agora pode testar os outros endpoints!

## 🛠️ Script SQL para Criar Usuários de Teste

```sql
-- Professor de teste (senha: senha123)
INSERT INTO TB_CAPP_PROFESSOR (
    ID_professor, nome_professor, email_professor, 
    senha_professor, telefone_professor, bio_professor, 
    experiencia_professor, data_cadastro
) VALUES (
    1, 
    'João Silva', 
    'joao@email.com', 
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '(11) 98765-4321',
    'Professor de Java e Spring Boot',
    '10 anos de experiência',
    SYSDATE
);

-- Aluno de teste (senha: senha123)
INSERT INTO TB_COURAPP_ALUNO (
    ID_aluno, nome_aluno, email_aluno, 
    senha_aluno, telefone_aluno, data_cadastro
) VALUES (
    1,
    'Maria Santos',
    'maria@email.com',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    '(11) 91234-5678',
    SYSDATE
);

COMMIT;
```

## 🚨 Troubleshooting

### Erro: "Bad credentials"
- Verifique se o email existe no banco
- Verifique se a senha está correta
- Se inseriu direto no banco, verifique se a senha está criptografada com BCrypt

### Erro: "Unauthorized" ou "403 Forbidden"
- Verifique se está enviando o token no header `Authorization`
- Verifique se o token está no formato: `Bearer {token}`
- Verifique se o token não expirou (24h)

### Erro: "Invalid JWT signature"
- O token está inválido ou foi modificado
- Faça login novamente para gerar um novo token

## 🔐 Boas Práticas de Segurança

1. **Nunca** guarde senhas em texto puro no banco
2. **Sempre** use HTTPS em produção
3. **Não** exponha o `jwt.secret` em repositórios públicos
4. **Configure** tempos de expiração adequados para tokens
5. **Implemente** refresh tokens para melhor experiência
6. **Valide** sempre os dados de entrada
7. **Use** senhas fortes (mínimo 8 caracteres, com letras, números e símbolos)

## 📚 Referências

- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [JWT.io](https://jwt.io/)
- [BCrypt Generator](https://bcrypt-generator.com/)
