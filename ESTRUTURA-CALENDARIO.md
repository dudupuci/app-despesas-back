# 📅 Sistema de Calendário Unificado

## 📋 Visão Geral

Sistema inspirado no app **Meu Assessor**, onde o usuário visualiza em um calendário todos os seus eventos, compromissos e movimentações financeiras previstas.

---

## 🎯 Estrutura do Sistema

### **3 Tipos de Eventos no Calendário:**

#### 1️⃣ **Compromissos** (nova entidade)
- **O que é:** Afazeres, tarefas, reuniões, consultas médicas, etc.
- **Tabela:** `compromissos`
- **Campos principais:**
  - `titulo`, `descricao`
  - `dataInicio`, `dataFim`
  - `diaInteiro` (boolean)
  - `prioridade` (BAIXA, MEDIA, ALTA, URGENTE)
  - `concluido` (boolean)
  - `localizacao`
  - `cor` (hex)
  - `lembrarEm` (data para notificação)

#### 2️⃣ **Eventos Recorrentes** (melhorada)
- **O que é:** Eventos que se repetem (aniversários, pagamentos fixos, etc.)
- **Tabela:** `eventos`
- **Campos principais:**
  - `titulo`, `descricao`
  - `dataInicio`, `dataFim`
  - `isRecorrente` (boolean)
  - `frequenciaRecorrencia` (DIARIA, SEMANAL, MENSAL, etc.)
  - `dataFimRecorrencia`
  - `cor` (hex)

#### 3️⃣ **Movimentações Previstas** (campos adicionados)
- **O que é:** Despesas ou receitas futuras (contas a pagar, salário a receber, etc.)
- **Tabela:** `movimentacoes`
- **Novos campos:**
  - `isPrevista` (boolean) - se é uma movimentação futura
  - `isRecorrente` (boolean) - se repete (ex: aluguel mensal)
  - `isEfetivada` (boolean) - se já foi realizada
  - `dataEfetivacao` - quando foi efetivada

---

## 🗂️ Estrutura de Arquivos Criados

### **📁 Enums**
```
models/enums/
├── TipoEvento.java              # COMPROMISSO, MOVIMENTACAO_PREVISTA, EVENTO_RECORRENTE
├── PrioridadeEvento.java        # BAIXA, MEDIA, ALTA, URGENTE
└── FrequenciaRecorrencia.java   # DIARIA, SEMANAL, MENSAL, etc.
```

### **📁 Entidades**
```
models/entities/
├── Compromisso.java             # Nova entidade para compromissos/tarefas
├── Evento.java                  # Melhorada para eventos recorrentes
└── Movimentacao.java            # Adicionados campos: isPrevista, isEfetivada, isRecorrente
```

### **📁 Repositories**
```
repositories/
├── CompromissoRepository.java   # Queries para compromissos
├── EventoRepository.java        # Queries para eventos recorrentes
└── MovimentacoesRepository.java # Adicionadas queries para movimentações previstas
```

### **📁 Services**
```
services/
└── CalendarioService.java       # Service unificado que agrega os 3 tipos de eventos
```

### **📁 Controllers**
```
controllers/
└── CalendarioController.java    # Endpoints do calendário
```

### **📁 DTOs**
```
controllers/dtos/calendario/
└── EventoCalendarioDto.java     # DTO unificado para todos os tipos de eventos
```

---

## 🌐 Endpoints da API

### **1. Listar eventos do calendário (período)**
```http
GET /api/calendario?dataInicio=01/03/2026&dataFim=31/03/2026
Authorization: Bearer {token}
```

**Response:**
```json
[
  {
    "id": 1,
    "tipoEvento": "COMPROMISSO",
    "titulo": "Consulta médica",
    "descricao": "Cardiologista",
    "dataInicio": "2026-03-15T10:00:00",
    "dataFim": "2026-03-15T11:00:00",
    "diaInteiro": false,
    "prioridade": "ALTA",
    "cor": "#FF5733",
    "concluido": false,
    "localizacao": "Hospital São Lucas"
  },
  {
    "id": 2,
    "tipoEvento": "MOVIMENTACAO_PREVISTA",
    "titulo": "Pagamento Aluguel",
    "descricao": "Aluguel março",
    "dataInicio": "2026-03-05T00:00:00",
    "valor": 1200.00,
    "tipoMovimentacao": "DESPESA",
    "categoriaNome": "Moradia",
    "isPrevista": true,
    "isEfetivada": false,
    "cor": "#FF5733"
  },
  {
    "id": 3,
    "tipoEvento": "EVENTO_RECORRENTE",
    "titulo": "Aniversário Maria",
    "descricao": "Aniversário da Maria",
    "dataInicio": "2026-03-20T00:00:00",
    "diaInteiro": true,
    "prioridade": "MEDIA",
    "cor": "#FFC107",
    "isRecorrente": true,
    "frequenciaRecorrencia": "ANUAL"
  }
]
```

---

### **2. Listar eventos de um dia específico**
```http
GET /api/calendario/dia?data=15/03/2026
Authorization: Bearer {token}
```

**Response:** (mesmo formato acima, só eventos do dia 15/03)

---

### **3. Listar alertas (movimentações previstas não efetivadas)**
```http
GET /api/calendario/alertas
Authorization: Bearer {token}
```

**Response:**
```json
[
  {
    "id": 2,
    "tipoEvento": "MOVIMENTACAO_PREVISTA",
    "titulo": "Pagamento Aluguel",
    "dataInicio": "2026-03-05T00:00:00",
    "valor": 1200.00,
    "isPrevista": true,
    "isEfetivada": false
  }
]
```

---

## 🎨 Como Funciona no Frontend

### **Visualização no Calendário:**

1. **Cores diferentes por tipo:**
   - 🟦 Compromissos: Azul/Personalizado
   - 🟥 Despesas Previstas: Vermelho
   - 🟩 Receitas Previstas: Verde
   - 🟨 Eventos Recorrentes: Amarelo/Personalizado

2. **Informações exibidas:**
   - Título do evento
   - Horário (se não for dia inteiro)
   - Valor (se for movimentação)
   - Ícone de prioridade (se urgente)
   - Ícone de conclusão (se compromisso concluído)

3. **Interações:**
   - Clicar no evento → Ver detalhes
   - Marcar compromisso como concluído
   - Efetivar movimentação prevista
   - Criar novo evento/compromisso/movimentação

---

## 📊 Exemplos de Uso

### **Criar Compromisso:**
```http
POST /api/compromissos
{
  "titulo": "Reunião com cliente",
  "descricao": "Apresentar proposta",
  "dataInicio": "15/03/2026 14:00",
  "dataFim": "15/03/2026 15:30",
  "diaInteiro": false,
  "prioridade": "ALTA",
  "localizacao": "Escritório Centro",
  "cor": "#3B82F6"
}
```

### **Criar Movimentação Prevista:**
```http
POST /api/movimentacoes
{
  "titulo": "Receber Salário",
  "descricao": "Salário março/2026",
  "valor": 5000.00,
  "dataDaMovimentacao": "05/03/2026",
  "tipoMovimentacao": "ENTRADA",
  "categoriaId": "uuid-da-categoria",
  "isPrevista": true,
  "isRecorrente": true
}
```

### **Criar Evento Recorrente:**
```http
POST /api/eventos
{
  "titulo": "Pagamento Netflix",
  "descricao": "Assinatura mensal",
  "dataInicio": "01/03/2026",
  "isRecorrente": true,
  "frequenciaRecorrencia": "MENSAL",
  "cor": "#E50914"
}
```

### **Efetivar Movimentação Prevista:**
```http
PATCH /api/movimentacoes/{id}/efetivar
```

### **Marcar Compromisso como Concluído:**
```http
PATCH /api/compromissos/{id}/concluir
```

---

## ✅ Vantagens desta Estrutura

1. ✅ **Calendário Unificado:** Tudo em um só lugar
2. ✅ **Flexibilidade:** Diferentes tipos de eventos convivem
3. ✅ **Controle Financeiro:** Visualizar despesas/receitas futuras
4. ✅ **Organização Pessoal:** Compromissos e tarefas organizados
5. ✅ **Eventos Recorrentes:** Não precisa cadastrar todo mês
6. ✅ **Alertas:** Notificações de contas a pagar/receber
7. ✅ **Cores Personalizadas:** Cada usuário customiza seu calendário
8. ✅ **Histórico:** Saber quando efetivou movimentações
9. ✅ **Prioridades:** Focar no que é mais importante

---

## 🚀 Próximos Passos Sugeridos

1. **Criar Controllers CRUD completos:**
   - CompromissosController
   - EventosController
   - (MovimentacoesController já existe, adicionar endpoints de efetivação)

2. **Implementar Notificações:**
   - Alertas de compromissos próximos
   - Lembretes de contas a pagar

3. **Recorrência Automática:**
   - Job para criar automaticamente instâncias de eventos recorrentes

4. **Dashboard:**
   - Visão geral do dia/semana
   - Tarefas pendentes
   - Contas a pagar hoje

5. **Integração com IA (WhatsApp):**
   - "Tenho consulta amanhã às 10h" → Cria compromisso
   - "Paguei a conta de luz" → Efetiva movimentação prevista

---

## 💡 Dicas de Implementação no Frontend

### **Bibliotecas Recomendadas:**
- **FullCalendar** (React/Vue/Angular)
- **React Big Calendar**
- **Vue Cal**

### **Features UI:**
- Visualização Mensal/Semanal/Diária
- Drag-and-drop para realocar eventos
- Filtros por tipo de evento
- Modal de criação rápida
- Badge de quantidade de eventos por dia
- Timeline do dia atual

---

**Estrutura criada com sucesso! 🎉**
Todos os arquivos necessários foram gerados e estão prontos para uso.

