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
 * Service de Chatbot usando Groq AI (grátis!)
 *
 * Nota: Usa OpenAiChatModel mas aponta para Groq via base-url no application.yml
 * A API do Groq é compatível com OpenAI, então funciona perfeitamente!
 */
@Service
@Slf4j
public class ChatBotService {

    private final OpenAiChatModel chatModel; // ✅ Funciona com Groq via base-url!

    public ChatBotService(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String processar(String mensagem) {
        log.info("📩 Processando mensagem com Groq: {}", mensagem);

        String systemPrompt = """
            Você é um assistente financeiro do app AppDespesas configurado pelo Eduardo.
            Suas funções são:
            - Verificar se o usuário tem registro no AppDespesas
            - Se ele tiver registro, validar a autenticação (mas não peça senha em hipótese alguma)
            - Se ele não tiver registro, oriente-o a criar uma conta no AppDespesas
            - Ajudar usuários a registrar despesas e entradas.
            
            Quando o usuário descrever uma despesa ou entrada, extraia:
            ======================JSON - INICIO======================
            - Tipo (obrigátorio): DESPESA ou ENTRADA
            - Valor em reais (obrigátorio)
            - Descrição (opcional)
            - Categoria (obrigátorio) (Alimentação, Transporte, Lazer, etc)
            - Data (se não especificada, use a data atual)
             ======================JSON - FIM======================
            Observação:
            - Quando tiver pelo menos todos os campos obrigatórios, retorne APENAS o JSON solicitado, sem texto adicional.
            - Se faltar algum campo obrigatório, peça educadamente que o usuário forneça as informações faltantes.
            - Nunca retorne nada que não seja o JSON quando todos os campos obrigatórios estiverem presentes.
            - Sempre retorne o JSON no formato correto, com aspas duplas.
            - Peça para o usuário confirmar as informações antes de registrar a despesa ou entrada.
            - Caso ele queira adicionar ou editar algum campo, permita que ele faça isso antes de finalizar o registro.
            - Lembre-se de ser educado e prestativo.
            
            Seja breve e objetivo.
            """;

        try {
            log.info("🔧 Criando SystemMessage e UserMessage...");
            Message systemMessage = new SystemMessage(systemPrompt);
            Message userMessage = new UserMessage(mensagem);

            log.info("🔧 Criando Prompt com {} mensagens", List.of(systemMessage, userMessage).size());
            Prompt prompt = new Prompt(List.of(systemMessage, userMessage));

            log.info("🚀 Chamando Groq API...");
            ChatResponse response = chatModel.call(prompt);

            String resposta = response.getResult().getOutput().getText();
            log.info("✅ Resposta do Groq recebida com sucesso: {}", resposta);
            return resposta;

        } catch (Exception e) {
            log.error("❌ Erro ao processar com Groq", e);
            log.error("❌ Tipo do erro: {}", e.getClass().getName());
            log.error("❌ Mensagem do erro: {}", e.getMessage());
            if (e.getCause() != null) {
                log.error("❌ Causa raiz: {}", e.getCause().getMessage());
            }
            return "Desculpe, não consegui processar sua mensagem no momento.";
        }
    }
}
