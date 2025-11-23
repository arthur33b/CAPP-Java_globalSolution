# DEPLOY - Guia de Deploy em Nuvem

## 🚀 Opções de Deploy

### 1. **Oracle Cloud (Recomendado para Oracle Database)**

#### Pré-requisitos:
- Conta Oracle Cloud (Free Tier disponível)
- Oracle Autonomous Database configurado
- Oracle Cloud Infrastructure (OCI) CLI instalado

#### Passos:

1. **Configurar o Banco de Dados:**
```bash
# Criar Autonomous Database no Oracle Cloud
# Baixar o Wallet de conexão
# Atualizar application-prod.properties com as credenciais
```

2. **Build da aplicação:**
```bash
mvn clean package -DskipTests
```

3. **Deploy usando OCI:**
```bash
# Criar Container Instance
oci compute-container-instance create \
  --compartment-id <seu-compartment-id> \
  --availability-domain <seu-ad> \
  --shape CI.Standard.E4.Flex \
  --shape-config '{"ocpus":1.0,"memoryInGBs":4.0}' \
  --containers file://container-config.json
```

---

### 2. **AWS (Amazon Web Services)**

#### Opções:
- **Elastic Beanstalk** (mais fácil)
- **ECS** (containerizado)
- **EC2** (mais controle)

#### Deploy com Elastic Beanstalk:

1. **Instalar EB CLI:**
```bash
pip install awsebcli
```

2. **Inicializar:**
```bash
eb init -p java-17 capp-api --region us-east-1
```

3. **Criar ambiente:**
```bash
eb create capp-api-prod
```

4. **Configurar variáveis de ambiente:**
```bash
eb setenv DATABASE_URL=jdbc:oracle:thin:@//seu-host:1521/xe \
         DATABASE_USERNAME=usuario \
         DATABASE_PASSWORD=senha \
         JWT_SECRET=sua-chave-secreta
```

5. **Deploy:**
```bash
eb deploy
```

---

### 3. **Azure**

#### Deploy usando Azure App Service:

1. **Instalar Azure CLI:**
```bash
# Windows
choco install azure-cli

# Linux/Mac
curl -sL https://aka.ms/InstallAzureCLIDeb | sudo bash
```

2. **Login:**
```bash
az login
```

3. **Criar Resource Group:**
```bash
az group create --name capp-api-rg --location eastus
```

4. **Criar App Service Plan:**
```bash
az appservice plan create \
  --name capp-api-plan \
  --resource-group capp-api-rg \
  --sku B1 \
  --is-linux
```

5. **Deploy:**
```bash
az webapp create \
  --resource-group capp-api-rg \
  --plan capp-api-plan \
  --name capp-api \
  --runtime "JAVA:17-java17"

az webapp deployment source config-zip \
  --resource-group capp-api-rg \
  --name capp-api \
  --src target/CAPP-API-1.0-SNAPSHOT.jar
```

6. **Configurar variáveis:**
```bash
az webapp config appsettings set \
  --resource-group capp-api-rg \
  --name capp-api \
  --settings DATABASE_URL=jdbc:oracle:thin:@//host:1521/xe \
             DATABASE_USERNAME=usuario \
             DATABASE_PASSWORD=senha \
             JWT_SECRET=chave-secreta
```

---

### 4. **Heroku (Mais Simples)**

1. **Criar conta no Heroku**

2. **Instalar Heroku CLI:**
```bash
# Windows
choco install heroku-cli

# Mac
brew tap heroku/brew && brew install heroku
```

3. **Login:**
```bash
heroku login
```

4. **Criar app:**
```bash
heroku create capp-api
```

5. **Adicionar Oracle Database addon (se disponível) ou configurar conexão externa**

6. **Configurar variáveis:**
```bash
heroku config:set DATABASE_URL=jdbc:oracle:thin:@//host:1521/xe
heroku config:set DATABASE_USERNAME=usuario
heroku config:set DATABASE_PASSWORD=senha
heroku config:set JWT_SECRET=sua-chave-secreta
heroku config:set SPRING_PROFILES_ACTIVE=prod
```

7. **Deploy:**
```bash
git push heroku main
```

---

### 5. **Google Cloud Platform (GCP)**

#### Deploy usando Cloud Run:

1. **Build da imagem Docker:**
```bash
# Criar Dockerfile na raiz do projeto
docker build -t gcr.io/seu-projeto-id/capp-api .
```

2. **Push para GCR:**
```bash
docker push gcr.io/seu-projeto-id/capp-api
```

3. **Deploy:**
```bash
gcloud run deploy capp-api \
  --image gcr.io/seu-projeto-id/capp-api \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --set-env-vars DATABASE_URL=jdbc:oracle:thin:@//host:1521/xe,DATABASE_USERNAME=usuario,DATABASE_PASSWORD=senha,JWT_SECRET=chave-secreta
```

---

## 📦 Dockerfile (Para deploy containerizado)

```dockerfile
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN apk add --no-cache maven
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "app.jar"]
```

---

## ✅ Checklist Pré-Deploy

- [ ] Banco de dados criado e acessível
- [ ] Variáveis de ambiente configuradas
- [ ] JWT_SECRET forte e seguro
- [ ] CORS configurado para domínio de produção
- [ ] SSL/HTTPS habilitado
- [ ] Logs configurados
- [ ] Backup do banco configurado
- [ ] Monitoramento configurado

---

## 🔒 Segurança

### Variáveis de Ambiente Obrigatórias:
- `DATABASE_URL`
- `DATABASE_USERNAME`
- `DATABASE_PASSWORD`
- `JWT_SECRET` (mínimo 256 bits)

### Gerar JWT_SECRET seguro:
```bash
openssl rand -base64 64
```

---

## 📊 Monitoramento

Após o deploy, acesse:
- **API**: `https://seu-dominio.com/api`
- **Swagger**: `https://seu-dominio.com/swagger-ui.html`
- **Health Check**: `https://seu-dominio.com/actuator/health` (se configurado)

---

## 🆘 Troubleshooting

### Erro de conexão com banco:
- Verificar firewall e regras de segurança
- Confirmar credenciais
- Testar conexão local primeiro

### Erro JWT:
- Verificar se JWT_SECRET está configurado
- Verificar formato do token

### Erro 503/504:
- Aumentar timeout
- Verificar recursos do servidor
- Checar logs da aplicação
