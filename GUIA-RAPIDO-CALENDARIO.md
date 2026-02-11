# 📅 Guia Rápido - Sistema de Calendário

## ✅ Sistema Implementado com Sucesso!

Você agora tem um **calendário unificado** que integra:
- 📋 **Compromissos** (tarefas, reuniões, consultas)
- 🎉 **Eventos Recorrentes** (aniversários, pagamentos fixos)
- 💰 **Movimentações Previstas** (contas a pagar/receber)

---

## 🚀 Endpoints Disponíveis

### **📅 Calendário Unificado**

#### Ver todos os eventos de um período
```http
GET /api/calendario?dataInicio=01/03/2026&dataFim=31/03/2026
Authorization: Bearer {token}
```

#### Ver eventos de um dia específico
```http
GET /api/calendario/dia?data=15/03/2026
Authorization: Bearer {token}
```

#### Ver alertas (movimentações previstas pendentes)
```http
GET /api/calendario/alertas
Authorization: Bearer {token}
```

---

### **📋 Compromissos**

#### Criar compromisso
```http
POST /api/compromissos
Authorization: Bearer {token}
Content-Type: application/json

{
  "titulo": "Consulta médica",
  "descricao": "Cardiologista Dr. Silva",
  "dataInicio": "2026-03-15T10:00:00",
  "dataFim": "2026-03-15T11:00:00",
  "diaInteiro": false,
  "prioridade": "ALTA",
  "localizacao": "Hospital São Lucas",
  "cor": "#FF5733",
  "lembrarEm": "2026-03-15T09:00:00"
}
```

#### Listar todos os compromissos
```http
GET /api/compromissos
```

#### Listar compromissos pendentes
```http
GET /api/compromissos/pendentes
```

#### Marcar como concluído
```http
PATCH /api/compromissos/{id}/concluir
```

#### Atualizar compromisso
```http
PUT /api/compromissos/{id}
```

#### Deletar compromisso
```http
DELETE /api/compromissos/{id}
```

---

### **🎉 Eventos Recorrentes**

#### Criar evento recorrente
```http
POST /api/eventos
Authorization: Bearer {token}
Content-Type: application/json

{
  "titulo": "Aniversário Maria",
  "descricao": "Festa da Maria",
  "dataInicio": "2026-03-20T00:00:00",
  "diaInteiro": true,
  "isRecorrente": true,
  "frequenciaRecorrencia": "ANUAL",
  "prioridade": "MEDIA",
  "cor": "#FFC107"
}
```

**Frequências disponíveis:**
- `DIARIA`
- `SEMANAL`
- `QUINZENAL`
- `MENSAL`
- `BIMESTRAL`
- `TRIMESTRAL`
- `SEMESTRAL`
- `ANUAL`

#### Listar todos os eventos
```http
GET /api/eventos
```

#### Listar apenas recorrentes
```http
GET /api/eventos/recorrentes
```

#### Atualizar evento
```http
PUT /api/eventos/{id}
```

#### Deletar evento
```http
DELETE /api/eventos/{id}
```

---

### **💰 Movimentações Previstas**

#### Criar movimentação prevista
```http
POST /api/movimentacoes
Authorization: Bearer {token}
Content-Type: application/json

{
  "titulo": "Pagamento Aluguel",
  "descricao": "Aluguel março/2026",
  "valor": 1200.00,
  "dataDaMovimentacao": "05/03/2026",
  "tipoMovimentacao": "DESPESA",
  "categoriaId": "uuid-da-categoria",
  "isPrevista": true,
  "isRecorrente": true
}
```

#### Efetivar movimentação prevista
```http
PATCH /api/movimentacoes/{id}/efetivar
```

#### Listar movimentações previstas pendentes
```http
GET /api/movimentacoes/previstas/pendentes
```

---

## 🎨 Valores dos Enums

### **PrioridadeEvento**
- `BAIXA`
- `MEDIA`
- `ALTA`
- `URGENTE`

### **TipoMovimentacao**
- `DESPESA`
- `ENTRADA`

### **FrequenciaRecorrencia**
- `DIARIA`
- `SEMANAL`
- `QUINZENAL`
- `MENSAL`
- `BIMESTRAL`
- `TRIMESTRAL`
- `SEMESTRAL`
- `ANUAL`

---

## 🎯 Exemplos de Uso no Frontend

### **1. Visualizar calendário do mês**
```javascript
const response = await fetch(
  '/api/calendario?dataInicio=01/03/2026&dataFim=31/03/2026',
  {
    headers: {
      'Authorization': `Bearer ${token}`
    }
  }
);
const eventos = await response.json();

// Retorna array com todos os tipos:
// - COMPROMISSO
// - EVENTO_RECORRENTE
// - MOVIMENTACAO_PREVISTA
```

### **2. Criar compromisso**
```javascript
await fetch('/api/compromissos', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    titulo: "Reunião com cliente",
    dataInicio: "2026-03-15T14:00:00",
    dataFim: "2026-03-15T15:30:00",
    prioridade: "ALTA",
    cor: "#3B82F6"
  })
});
```

### **3. Marcar compromisso como concluído**
```javascript
await fetch(`/api/compromissos/${id}/concluir`, {
  method: 'PATCH',
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
```

### **4. Efetivar movimentação prevista**
```javascript
await fetch(`/api/movimentacoes/${id}/efetivar`, {
  method: 'PATCH',
  headers: {
    'Authorization': `Bearer ${token}`
  }
});
```

### **5. Ver alertas do dia**
```javascript
const alertas = await fetch('/api/calendario/alertas', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
}).then(r => r.json());

// Mostra badge com quantidade de alertas
console.log(`Você tem ${alertas.length} contas para pagar`);
```

---

## 📊 Estrutura do Banco de Dados

### **Tabela: compromissos**
```sql
CREATE TABLE compromissos (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    descricao VARCHAR(1000),
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP,
    dia_inteiro BOOLEAN DEFAULT FALSE,
    prioridade VARCHAR(20) NOT NULL DEFAULT 'MEDIA',
    localizacao VARCHAR(500),
    concluido BOOLEAN DEFAULT FALSE,
    data_conclusao TIMESTAMP,
    lembrar_em TIMESTAMP,
    cor VARCHAR(50),
    observacoes VARCHAR(500),
    usuario_id UUID NOT NULL,
    data_criacao TIMESTAMP DEFAULT NOW(),
    data_atualizacao TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (usuario_id) REFERENCES usuarios_sistema(id)
);
```

### **Tabela: eventos**
```sql
CREATE TABLE eventos (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    descricao VARCHAR(1000),
    data_inicio TIMESTAMP NOT NULL,
    data_fim TIMESTAMP,
    is_recorrente BOOLEAN DEFAULT FALSE,
    frequencia_recorrencia VARCHAR(20),
    data_fim_recorrencia TIMESTAMP,
    prioridade VARCHAR(20) NOT NULL DEFAULT 'MEDIA',
    cor VARCHAR(50),
    dia_inteiro BOOLEAN DEFAULT FALSE,
    observacoes VARCHAR(500),
    usuario_id UUID NOT NULL,
    data_criacao TIMESTAMP DEFAULT NOW(),
    data_atualizacao TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (usuario_id) REFERENCES usuarios_sistema(id)
);
```

### **Tabela: movimentacoes (campos adicionados)**
```sql
ALTER TABLE movimentacoes ADD COLUMN is_prevista BOOLEAN DEFAULT FALSE;
ALTER TABLE movimentacoes ADD COLUMN is_recorrente BOOLEAN DEFAULT FALSE;
ALTER TABLE movimentacoes ADD COLUMN is_efetivada BOOLEAN DEFAULT FALSE;
ALTER TABLE movimentacoes ADD COLUMN data_efetivacao TIMESTAMP;
```

---

## 🔥 Próximas Features Sugeridas

1. **Notificações Push/Email**
   - Lembretes de compromissos
   - Alertas de contas a pagar

2. **Recorrência Automática**
   - Job que cria automaticamente instâncias de eventos recorrentes

3. **Dashboard**
   - Resumo do dia/semana
   - Tarefas pendentes
   - Contas a pagar hoje

4. **Integração com IA**
   - "Tenho consulta amanhã às 10h" → cria compromisso via WhatsApp
   - "Paguei a conta de luz" → efetiva movimentação prevista

5. **Sincronização com Google Calendar**
   - Importar/exportar eventos

6. **Categorias para Compromissos**
   - Trabalho, Pessoal, Saúde, etc.

7. **Anexos**
   - Upload de arquivos em compromissos (recibos, documentos)

---

**🎉 Sistema de Calendário implementado e pronto para uso!**

