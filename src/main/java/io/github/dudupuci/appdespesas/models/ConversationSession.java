package io.github.dudupuci.appdespesas.models;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa uma sessão de conversa com histórico de mensagens
 */
@Data
public class ConversationSession {

    private String chatId;
    private String session;
    private List<Message> messages;
    private LocalDateTime lastActivity;
    private boolean active;

    public ConversationSession(String chatId, String session) {
        this.chatId = chatId;
        this.session = session;
        this.messages = new ArrayList<>();
        this.lastActivity = LocalDateTime.now();
        this.active = true;
    }

    public void addUserMessage(String text) {
        messages.add(new Message("user", text));
        updateActivity();
    }

    public void addAssistantMessage(String text) {
        messages.add(new Message("assistant", text));
        updateActivity();
    }

    private void updateActivity() {
        this.lastActivity = LocalDateTime.now();
    }

    public boolean isExpired(int timeoutMinutes) {
        return LocalDateTime.now().minusMinutes(timeoutMinutes).isAfter(lastActivity);
    }

    public void endSession() {
        this.active = false;
    }

    @Data
    public static class Message {
        private String role; // "user" ou "assistant"
        private String content;
        private LocalDateTime timestamp;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
            this.timestamp = LocalDateTime.now();
        }
    }
}

