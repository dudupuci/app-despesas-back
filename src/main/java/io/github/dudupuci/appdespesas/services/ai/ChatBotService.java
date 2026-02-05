package io.github.dudupuci.appdespesas.services.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;

import java.util.List;

/**
 * Service de Chatbot usando Groq AI com suporte a contexto de conversa
 */
@Service
@Slf4j
public class ChatBotService {

    private final OpenAiChatModel chatModel;
    private final ConversationService conversationService;

    public ChatBotService(OpenAiChatModel chatModel, ConversationService conversationService) {
        this.chatModel = chatModel;
        this.conversationService = conversationService;
    }

    /**
     * Processa mensagem COM contexto da conversa
     */
    public String processarComContexto(String chatId, String session, String mensagemUsuario) {
        log.info("📩 Processando mensagem com contexto: chatId={}, mensagem={}", chatId, mensagemUsuario);

        // Obter ou criar sessão de conversa
        conversationService.getOrCreateSession(chatId, session);

        // Obter histórico da conversa
        String historico = conversationService.getFormattedHistory(chatId);

        String systemPrompt = """
            Você é um assistente financeiro do app AppDespesas.
            Sua função é ajudar usuários a registrar despesas e entradas através de uma conversa natural.
            
            Quando o usuário descrever uma despesa ou entrada, extraia:
            - Tipo: DESPESA ou ENTRADA
            - Valor em reais
            - Descrição
            - Categoria sugerida (Alimentação, Transporte, Lazer, Saúde, etc)
            
            Mantenha uma conversa natural e amigável. Se faltarem informações, pergunte ao usuário.
            
            Comandos especiais:
            - "sair" ou "tchau": Encerra a conversa
            - "ajuda": Mostra o que você pode fazer
            
            Seja breve e objetivo nas respostas.
            """;

        try {
            // Adicionar mensagem do usuário ao histórico
            conversationService.addUserMessage(chatId, mensagemUsuario);

            // Verificar comandos especiais
            String mensagemLower = mensagemUsuario.toLowerCase().trim();
            if (mensagemLower.equals("sair") || mensagemLower.equals("tchau") || mensagemLower.equals("encerrar")) {
                conversationService.endSession(chatId);
                return "Conversa encerrada! Até logo! 👋";
            }

            // Montar prompt com histórico
            String promptComHistorico = historico.isEmpty()
                ? mensagemUsuario
                : historico + "\n\nNova mensagem do usuário: " + mensagemUsuario;

            Message systemMessage = new SystemMessage(systemPrompt);
            Message userMessage = new UserMessage(promptComHistorico);

            Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

            log.info("🚀 Chamando Groq API com contexto...");
            ChatResponse response = chatModel.call(prompt);

            String resposta = response.getResult().getOutput().getText();
            log.info("✅ Resposta do Groq recebida: {}", resposta);

            // Adicionar resposta do assistente ao histórico
            conversationService.addAssistantMessage(chatId, resposta);

            return resposta;

        } catch (Exception e) {
            log.error("❌ Erro ao processar com IA", e);
            return "Desculpe, não consegui processar sua mensagem no momento. Pode tentar novamente?";
        }
    }

}
