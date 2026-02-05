# 🐳 Guia Docker - AppDespesas

## 📋 Arquivos Docker

- `Dockerfile` - Build da aplicação Spring Boot
- `docker-compose.yml` - Ambiente de **PRODUÇÃO** (Backend + PostgreSQL + WAHA)
- `docker-compose.test.yml` - Ambiente de **DESENVOLVIMENTO** (PostgreSQL + WAHA)
- `.dockerignore` - Arquivos ignorados no build

---

## 🧪 Ambiente de DESENVOLVIMENTO (test)

### **O que roda:**
- ✅ PostgreSQL (porta 5432)
- ✅ WAHA (porta 3001)
- ❌ Backend (roda localmente no IntelliJ)

### **Como usar:**

```bash
# 1. Iniciar serviços
docker-compose -f docker-compose.test.yml up -d

# 2. Ver logs
docker-compose -f docker-compose.test.yml logs -f

# 3. Parar serviços
docker-compose -f docker-compose.test.yml down

# 4. Limpar volumes (CUIDADO: apaga dados!)
docker-compose -f docker-compose.test.yml down -v
```

### **Acessar serviços:**
- PostgreSQL: `localhost:5432`
- WAHA Dashboard: `http://localhost:3001`

---

## 🏭 Ambiente de PRODUÇÃO

### **O que roda:**
- ✅ PostgreSQL (porta 5432)
- ✅ Backend Spring Boot (porta 8080)
- ✅ WAHA (porta 3001)

### **Como usar:**

```bash
# 1. Criar arquivo .env com variáveis
cp .env.example .env
# Edite o .env com suas credenciais

# 2. Build e iniciar
docker-compose up -d --build

# 3. Ver logs de todos os serviços
docker-compose logs -f

# 4. Ver logs apenas do backend
docker-compose logs -f backend

# 5. Reiniciar apenas o backend
docker-compose restart backend

# 6. Parar tudo
docker-compose down

# 7. Rebuild apenas o backend
docker-compose up -d --build backend
```

### **Acessar serviços:**
- Backend API: `http://localhost:8080/api`
- Health Check: `http://localhost:8080/api/health`
- WAHA Dashboard: `http://localhost:3001`
- PostgreSQL: `localhost:5432`

---

## 🔐 Variáveis de Ambiente Necessárias

Crie arquivo `.env` na raiz:

```env
# Database
POSTGRES_DB=appdespesas
POSTGRES_USER=postgres
POSTGRES_PASSWORD=sua_senha_super_secreta

# Groq AI
GROQ_API_KEY=gsk_sua_chave_aqui

# JWT
JWT_SECRET=sua_chave_jwt_256_bits_aqui

# Webhook
WEBHOOK_API_KEY=wh_8F3kL9mN2pQ7rT1vY4xZ6aB0cD5eG8hJ

# WAHA
WAHA_API_KEY=00bc0f3cd14d4166a1f357077585f197
WAHA_SWAGGER_ENABLED=false
WAHA_SWAGGER_USER=admin
WAHA_SWAGGER_PASS=senha_segura

# Frontend
FRONTEND_URL=https://seu-frontend.vercel.app

# Logs
WAHA_LOG_LEVEL=info
```

---

## 📊 Healthchecks

Todos os serviços têm healthchecks configurados:

```bash
# Verificar status dos containers
docker-compose ps

# Exemplo de saída:
# NAME                  STATUS                    HEALTH
# backend-appdespesas   Up 2 minutes (healthy)
# postgres-appdespesas  Up 2 minutes (healthy)
# waha-whatsapp         Up 2 minutes
```

---

## 🧹 Comandos Úteis

```bash
# Ver todos os containers
docker ps

# Ver logs em tempo real
docker-compose logs -f backend

# Entrar no container do backend
docker exec -it backend-appdespesas sh

# Entrar no PostgreSQL
docker exec -it postgres-appdespesas psql -U postgres -d appdespesas

# Ver uso de recursos
docker stats

# Limpar imagens não usadas
docker system prune -a

# Rebuild sem cache
docker-compose build --no-cache

# Ver volumes
docker volume ls

# Remover volume específico
docker volume rm appdespesas_postgres_data
```

---

## 🚀 Deploy em Produção

### **1. Render.com:**

1. Conecte seu repositório GitHub
2. Selecione "Docker" como ambiente
3. Adicione variáveis de ambiente do `.env`
4. Deploy!

### **2. Railway.app:**

1. New Project → Deploy from GitHub
2. Adicione variáveis de ambiente
3. Railway detecta o Dockerfile automaticamente
4. Deploy!

### **3. VPS (DigitalOcean, AWS, etc):**

```bash
# 1. Clonar repositório
git clone https://github.com/seu-usuario/app-despesas-back.git
cd app-despesas-back

# 2. Criar .env com variáveis de produção
nano .env

# 3. Iniciar
docker-compose up -d --build

# 4. Ver logs
docker-compose logs -f
```

---

## 🔧 Troubleshooting

### **Backend não inicia:**

```bash
# Ver logs detalhados
docker-compose logs backend

# Verificar variáveis de ambiente
docker-compose config
```

### **PostgreSQL não conecta:**

```bash
# Ver logs do PostgreSQL
docker-compose logs postgres

# Verificar se está healthy
docker-compose ps

# Testar conexão manualmente
docker exec -it postgres-appdespesas psql -U postgres -d appdespesas
```

### **WAHA não conecta:**

```bash
# Ver logs do WAHA
docker-compose logs waha

# Acessar dashboard
http://localhost:3001
```

### **Rebuild completo:**

```bash
# Parar tudo
docker-compose down -v

# Rebuild sem cache
docker-compose build --no-cache

# Iniciar novamente
docker-compose up -d
```

---

## ✅ Checklist de Deploy

- [ ] Arquivo `.env` criado com todas as variáveis
- [ ] Senhas fortes configuradas (não usar defaults!)
- [ ] `WAHA_SWAGGER_ENABLED=false` em produção
- [ ] Frontend URL correto em `FRONTEND_URL`
- [ ] Firewall configurado (apenas portas necessárias abertas)
- [ ] Backups do PostgreSQL configurados
- [ ] Logs sendo monitorados
- [ ] SSL/HTTPS configurado (Nginx/Traefik na frente)

---

## 📚 Arquitetura

```
┌─────────────────────────────────────────┐
│           Docker Network                │
│                                         │
│  ┌──────────┐  ┌──────────┐  ┌───────┐│
│  │PostgreSQL│←→│  Backend │←→│ WAHA  ││
│  │  :5432   │  │   :8080  │  │ :3000 ││
│  └──────────┘  └──────────┘  └───────┘│
│       ↓              ↓           ↓     │
│  [volume]      [health]    [volume]   │
└─────────────────────────────────────────┘
         ↑              ↑           ↑
    localhost:5432  localhost:8080  localhost:3001
```

---

**Agora você tem um ambiente Docker completo e profissional!** 🐳🚀

