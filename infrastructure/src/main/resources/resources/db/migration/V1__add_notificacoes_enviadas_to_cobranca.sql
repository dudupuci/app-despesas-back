-- ========================================
-- Script de Migração: Sistema de Notificações de Cobrança
-- Data: 08/03/2026
-- Descrição: Adiciona coluna para rastrear notificações enviadas
-- ========================================

-- Adicionar coluna para armazenar notificações enviadas (formato JSON)
ALTER TABLE cobranca
ADD COLUMN notificacoes_enviadas TEXT;

-- Comentário explicativo
COMMENT ON COLUMN cobranca.notificacoes_enviadas IS
'Armazena em JSON as notificações enviadas ao usuário. Formato: {"TIPO_NOTIFICACAO": "2026-03-08T14:30:00"}';

-- ========================================
-- Exemplos de dados que serão armazenados:
-- ========================================
-- {
--   "EMAIL_COBRANCA_CRIADA": "2026-03-08T14:30:00",
--   "SMS_VENCIMENTO_PROXIMO": "2026-03-10T09:00:00",
--   "EMAIL_PAGAMENTO_CONFIRMADO": "2026-03-15T16:45:00"
-- }

