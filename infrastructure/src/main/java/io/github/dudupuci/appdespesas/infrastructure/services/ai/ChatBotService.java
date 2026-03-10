package io.github.dudupuci.appdespesas.infrastructure.services.ai;

import io.github.dudupuci.appdespesas.application.services.ai.ConversationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service de Chatbot usando Groq AI — pertence à infrastructure pois depende de Spring AI.
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

    public String processarComContexto(String chatId, String session, String mensagemUsuario) {
        log.info("📩 Processando mensagem com contexto: chatId={}, mensagem={}", chatId, mensagemUsuario);

        conversationService.getOrCreateSession(chatId, session);
        String historico = conversationService.getFormattedHistory(chatId);

        String systemPrompt = """
            Você é um assistente financeiro do app Tudin, seu nome é Dudu, assistente do TudinAI!.
            Suas funções são:
            1) Ajudar o usuário a registrar suas despesas e receitas financeiras
              1.1) Extrair informações como:
              - Título da movimentação (obrigatório, ex: "Almoço no restaurante X", "Salário de junho", etc);
              - Descrição (opcional)
              - Valor (obrigatório, cheio ou decimal, ex: 50 ou 50.75);
              - Data (se não for fornecida, considere a data atual);
              - Tipo (obrigatório, DESPESA ou RECEITA, pode ser entendido com base no contexto, ex: "gastei, perdi, doei" indica DESPESA, "recebi, ganhei, herdei" indica RECEITA);
              - Categoria (obrigatório, se o usuário não fornecer, sugira com base na descrição, ex: "Alimentação", "Transporte", "Lazer", "Saúde", etc).
            2) Responder perguntas sobre o histórico financeiro do usuário, como:
              - "Quanto gastei no mês passado?"
              - "Qual foi minha maior despesa?"
              - "Quanto recebi no último trimestre?"
              - "Quais categorias de despesas mais usei?"
              2.1) Fornecer informações sobre saldo, total de despesas e receitas, etc.
              2.2) Passar relatórios em texto: balanço do dia, semana, mês, análise de categorias, dicas de economia.
            3) Responder dúvidas sobre o app e suas funcionalidades.
            4) Fornecer dicas financeiras e de organização de despesas.
            5) Manter uma conversa natural e amigável. Se faltarem informações obrigatórias, pergunte ao usuário.
            
            Nunca forneça:
            - Informações falsas ou inventadas.
            - Informações pessoais de usuários ou dados sensíveis.
                                
            Comandos especiais:
            - Palavras como "sair, encerrar, tchau, finalizar" encerram a conversa.
            
            Seja breve e objetivo nas respostas.
            """;

        try {
            conversationService.addUserMessage(chatId, mensagemUsuario);

            String mensagemLower = mensagemUsuario.toLowerCase().trim();
            if (mensagemLower.equals("sair") || mensagemLower.equals("tchau") || mensagemLower.equals("encerrar")) {
                conversationService.endSession(chatId);
                return "Conversa encerrada! Até logo! 👋";
            }

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

            conversationService.addAssistantMessage(chatId, resposta);
            return resposta;

        } catch (Exception e) {
            log.error("❌ Erro ao processar com IA", e);
            return "Desculpe, não consegui processar sua mensagem no momento. Pode tentar novamente?";
        }
    }
}

