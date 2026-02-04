package io.github.dudupuci.appdespesas.controllers.webhooks;

import io.github.dudupuci.appdespesas.controllers.dtos.request.waha.WahaMessageRequestDto;
import io.github.dudupuci.appdespesas.services.ai.ChatBotService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhooks")
@Slf4j
public class WebhooksController {

    private final ChatBotService chatBotService;

    public WebhooksController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping("/waha")
    public void handleWahaPostWebhook(@RequestBody WahaMessageRequestDto dto) {
        log.info("📱 Webhook WAHA recebido:");
        log.info("Chat ID: {}", dto.chatId());
        log.info("Session: {}", dto.session());

        if (dto.text() != null && !dto.text().isBlank()) {
            log.info("Mensagem: {}", dto.text());
            String resposta = chatBotService.processar(dto.text());
            log.info("Resposta gerada: {}", resposta);
            // Aqui você pode adicionar a lógica para enviar a resposta de volta via WAHA API
        } else {
            log.info("Nenhuma mensagem de texto recebida.");
        }
    }

}
