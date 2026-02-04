package io.github.dudupuci.appdespesas.controllers;

import io.github.dudupuci.appdespesas.services.ai.ChatBotService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/chatbot")
public class ChatBotController {

    private final ChatBotService chatbotService;

    public ChatBotController(ChatBotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    /**
     * Endpoint para enviar mensagem em TEXTO SIMPLES
     *
     * Exemplo:
     * POST /chatbot/mensagem
     * Content-Type: text/plain
     * Body: "Gastei 50 reais no almoço"
     */
    @PostMapping(value = "/mensagem", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Map<String, String>> enviarMensagemTexto(@RequestBody String mensagem) {
        String resposta = chatbotService.processar(mensagem);
        return ResponseEntity.ok(Map.of(
            "mensagem", mensagem,
            "resposta", resposta
        ));
    }

    /**
     * Endpoint para enviar mensagem em JSON
     *
     * Exemplo:
     * POST /chatbot/mensagem/json
     * Content-Type: application/json
     * Body: { "mensagem": "Gastei 50 reais no almoço" }
     */
    @PostMapping("/mensagem/json")
    public ResponseEntity<Map<String, String>> enviarMensagemJson(@RequestBody Map<String, String> body) {
        String mensagem = body.get("mensagem");
        if (mensagem == null || mensagem.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                "erro", "Campo 'mensagem' é obrigatório"
            ));
        }

        String resposta = chatbotService.processar(mensagem);
        return ResponseEntity.ok(Map.of(
            "mensagem", mensagem,
            "resposta", resposta
        ));
    }
}