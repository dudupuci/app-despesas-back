package io.github.dudupuci.appdespesas.services.ai;

import io.github.dudupuci.appdespesas.models.ConversationSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service para gerenciar sessões de conversa contínua
 */
@Service
@Slf4j
public class ConversationService {

    // Armazena conversas ativas em memória (por chatId)
    private final Map<String, ConversationSession> activeSessions = new ConcurrentHashMap<>();

    // Timeout de inatividade (30 minutos)
    private static final int SESSION_TIMEOUT_MINUTES = 30;

    /**
     * Obtém ou cria uma sessão de conversa
     */
    public ConversationSession getOrCreateSession(String chatId, String session) {
        // Limpar sessões expiradas
        cleanExpiredSessions();

        // Obter ou criar sessão
        return activeSessions.computeIfAbsent(chatId, id -> {
            log.info("🆕 Nova sessão de conversa iniciada: chatId={}", chatId);
            return new ConversationSession(chatId, session);
        });
    }

    /**
     * Adiciona mensagem do usuário ao histórico
     */
    public void addUserMessage(String chatId, String message) {
        ConversationSession session = activeSessions.get(chatId);
        if (session != null) {
            session.addUserMessage(message);
            log.debug("💬 Mensagem do usuário adicionada ao histórico: {}", message);
        }
    }

    /**
     * Adiciona resposta do assistente ao histórico
     */
    public void addAssistantMessage(String chatId, String message) {
        ConversationSession session = activeSessions.get(chatId);
        if (session != null) {
            session.addAssistantMessage(message);
            log.debug("🤖 Resposta do assistente adicionada ao histórico: {}", message);
        }
    }

    /**
     * Encerra uma sessão de conversa
     */
    public void endSession(String chatId) {
        ConversationSession session = activeSessions.get(chatId);
        if (session != null) {
            session.endSession();
            activeSessions.remove(chatId);
            log.info("🔚 Sessão de conversa encerrada: chatId={}", chatId);
        }
    }

    /**
     * Verifica se há uma sessão ativa
     */
    public boolean hasActiveSession(String chatId) {
        return activeSessions.containsKey(chatId);
    }

    /**
     * Obtém o histórico de mensagens formatado para a IA
     */
    public String getFormattedHistory(String chatId) {
        ConversationSession session = activeSessions.get(chatId);
        if (session == null || session.getMessages().isEmpty()) {
            return "";
        }

        StringBuilder history = new StringBuilder();
        history.append("Histórico da conversa:\n\n");

        for (ConversationSession.Message msg : session.getMessages()) {
            String role = msg.getRole().equals("user") ? "Usuário" : "Assistente";
            history.append(String.format("%s: %s\n", role, msg.getContent()));
        }

        return history.toString();
    }

    /**
     * Limpa sessões expiradas (inativas por mais de X minutos)
     */
    private void cleanExpiredSessions() {
        activeSessions.entrySet().removeIf(entry -> {
            if (entry.getValue().isExpired(SESSION_TIMEOUT_MINUTES)) {
                log.info("🧹 Sessão expirada removida: chatId={}", entry.getKey());
                return true;
            }
            return false;
        });
    }

    /**
     * Retorna estatísticas das sessões ativas
     */
    public Map<String, Object> getStats() {
        return Map.of(
            "activeSessions", activeSessions.size(),
            "timeoutMinutes", SESSION_TIMEOUT_MINUTES
        );
    }
}

