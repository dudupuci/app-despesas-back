# 🚀 Guia Completo - Docker Compose Test & Produção + WhatsApp Bot

## 📋 Arquivos Criados

1. ✅ **Dockerfile** - Build do backend Spring Boot
2. ✅ **docker-compose.test.yml** - Para desenvolvimento local
3. ✅ **docker-compose.yml** - Para produção completa
4. ✅ **WahaService.java** - Service para enviar mensagens ao WhatsApp
5. ✅ **WebhooksController.java** - Atualizado com resposta automática
6. ✅ **.env.example** - Exemplo de variáveis de ambiente

---

## 🎯 Modo TEST (Desenvolvimento)

### O que roda:
- ✅ **WAHA** no Docker (porta 3001)
- ✅ **Backend** fora do Docker (localhost:8080)
- ✅ **PostgreSQL** local (localhost:5432)

### Como usar:

```bash
# 1. Iniciar apenas o WAHA
docker-compose -f docker-compose.test.yml up -d

# 2. Rodar backend no IntelliJ (modo debug)
mvn spring-boot:run

# 3. Acessar WAHA Dashboard
http://localhost:3001

# 4. Conectar WhatsApp escaneando QR Code
```

### Vantagens:
- ✅ Debug fácil no IntelliJ
- ✅ Hot reload do código
- ✅ Logs direto no console
- ✅ Fácil testar mudanças

---

## 🏭 Modo PRODUÇÃO (Deploy Completo)

### O que roda:
- ✅ **PostgreSQL** no Docker
- ✅ **Backend** no Docker
- ✅ **WAHA** no Docker
- ✅ Todos conversam via rede interna Docker

### Como usar:

```bash
# 1. Criar arquivo .env com suas configurações
cp .env.example .env
# Edite o .env com suas chaves reais

# 2. Build e iniciar todos os serviços
docker-compose up -d --build

# 3. Ver logs
docker-compose logs -f

# 4. Acessar WAHA Dashboard
http://localhost:3001

# 5. Acessar Backend API
http://localhost:8080/api/health
```

### Vantagens:
- ✅ Pronto para deploy em servidor
- ✅ Todos os serviços isolados
- ✅ Fácil escalar
- ✅ Persistência de dados com volumes

---

## 🔄 Fluxo Completo Funcionando

```
1. Usuário envia mensagem no WhatsApp
   "Gastei 50 reais no almoço"
          ↓
2. WAHA recebe a mensagem
          ↓
3. WAHA chama webhook do backend
   POST http://backend:8080/api/webhooks/waha
   Header: X-Webhook-Token: wh_...
   Body: { chatId, session, text }
          ↓
4. Backend valida API Key ✅
          ↓
5. Backend processa com Groq AI 🤖
   Resposta: "Entendi! Vou registrar uma DESPESA de R$ 50,00 em Alimentação..."
          ↓
6. Backend chama WAHA API
   POST http://waha:3000/api/sendText
   Body: { session, chatId, text: "resposta da IA" }
          ↓
7. WAHA envia resposta para o WhatsApp
          ↓
8. Usuário recebe a resposta automaticamente! ✅
```

---

## 🧪 Testar Localmente

### 1. Testar webhook manualmente:

```bash
curl -X POST http://localhost:8080/api/webhooks/waha \
  -H "Content-Type: application/json" \
  -H "X-Webhook-Token: wh_8F3kL9mN2pQ7rT1vY4xZ6aB0cD5eG8hJ" \
  -d '{
    "chatId": "5511999999999@c.us",
    "session": "default",
    "text": "Oi tudo bem?"
  }'
```

**Resposta esperada:**
```
Message processed and replied successfully
```

### 2. Verificar logs do backend:

```
📱 Webhook WAHA recebido:
Chat ID: 5511999999999@c.us
Session: default
📩 Mensagem: Oi tudo bem?
🤖 Resposta gerada pela IA: Tudo bem, sim! Como posso ajudar?
📤 Enviando mensagem para WAHA: session=default, chatId=5511999999999@c.us
✅ Mensagem enviada com sucesso para o WhatsApp
```

---

## 📋 Comandos Úteis

### Docker Compose Test:

```bash
# Iniciar
docker-compose -f docker-compose.test.yml up -d

# Parar
docker-compose -f docker-compose.test.yml down

# Ver logs
docker-compose -f docker-compose.test.yml logs -f waha
```

### Docker Compose Produção:

```bash
# Build e iniciar
docker-compose up -d --build

# Parar tudo
docker-compose down

# Parar e remover volumes (CUIDADO: apaga dados)
docker-compose down -v

# Ver logs de todos os serviços
docker-compose logs -f

# Ver logs apenas do backend
docker-compose logs -f backend

# Ver logs apenas do WAHA
docker-compose logs -f waha

# Reiniciar apenas o backend
docker-compose restart backend

# Rebuild apenas o backend
docker-compose up -d --build backend
```

---

## 🔐 Variáveis de Ambiente (.env)

Crie um arquivo `.env` na raiz do projeto:

```env
# Database
POSTGRES_DB=appdespesas
POSTGRES_USER=postgres
POSTGRES_PASSWORD=sua_senha_super_secreta

# Groq AI
GROQ_API_KEY=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

# Webhook
WEBHOOK_API_KEY=wh_8F3kL9mN2pQ7rT1vY4xZ6aB0cD5eG8hJ

# JWT
JWT_SECRET=gere_um_secret_forte_aqui

# Frontend
FRONTEND_URL=https://seu-frontend.vercel.app

# WAHA
WAHA_LOG_LEVEL=info
WAHA_SWAGGER_ENABLED=false
```

---

## 🚨 Troubleshooting

### Problema: Backend não consegue conectar no PostgreSQL

**Solução:** Verifique se o PostgreSQL está rodando:

```bash
docker-compose ps
docker-compose logs postgres
```

### Problema: WAHA não consegue chamar o backend

**Solução:** Verifique se o backend está rodando e saudável:

```bash
docker-compose logs backend
curl http://localhost:8080/api/health
```

### Problema: Backend não envia resposta de volta

**Solução:** Verifique se a URL do WAHA está correta:

```bash
# Test (localhost)
waha.api.url=http://localhost:3001

# Produção (Docker)
waha.api.url=http://waha:3000
```

### Problema: QR Code não aparece no WAHA

**Solução:** Veja os logs do WAHA:

```bash
docker-compose logs -f waha
```

O QR Code será impresso nos logs.

---

## 🎯 Deploy em Produção (Render/Railway)

### 1. Fazer push do código para GitHub

```bash
git add .
git commit -m "Add Docker support and WhatsApp integration"
git push origin main
```

### 2. No Render.com:

1. Criar **Web Service** → Conectar ao GitHub
2. Selecionar **Docker** como Environment
3. Adicionar variáveis de ambiente (do .env)
4. Deploy!

### 3. No Railway.app:

1. New Project → Deploy from GitHub
2. Adicionar variáveis de ambiente
3. Railway detecta o Dockerfile automaticamente
4. Deploy!

---

## ✅ Checklist Final

- [ ] Arquivo `.env` criado com suas chaves
- [ ] WAHA rodando (test ou prod)
- [ ] Backend rodando (test ou prod)
- [ ] PostgreSQL rodando (prod)
- [ ] WhatsApp conectado (QR Code escaneado)
- [ ] Teste enviando mensagem para o bot
- [ ] Bot responde automaticamente ✅

---

## 🎉 Resultado Final

Agora você tem:

1. ✅ **Chatbot WhatsApp** funcionando
2. ✅ **IA Groq** processando mensagens gratuitamente
3. ✅ **Resposta automática** para o usuário
4. ✅ **Docker Compose** para test e produção
5. ✅ **Pronto para deploy** em qualquer servidor

**Seu bot está funcionando end-to-end!** 🤖💬✨

