package io.github.dudupuci.appdespesas.controllers.webhooks;

import io.github.dudupuci.appdespesas.controllers.dtos.request.waha.WahaMessageRequestDto;
import io.github.dudupuci.appdespesas.services.ai.ChatBotService;
import io.github.dudupuci.appdespesas.services.waha.WahaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/webhooks")
@Slf4j
public class WebhooksController {

    private final ChatBotService chatBotService;
    private final WahaService wahaService;

    @Value("${app.webhook.api-key}")
    private String webhookApiKey;

    public WebhooksController(ChatBotService chatBotService, WahaService wahaService) {
        this.chatBotService = chatBotService;
        this.wahaService = wahaService;
    }

    @PostMapping("/waha")
    public ResponseEntity<?> handleWahaPostWebhook(
            @RequestHeader(value = "X-Webhook-Token", required = false) String token,
            @RequestBody WahaMessageRequestDto dto
    ) {
        /*
        if (token == null || !token.equals(webhookApiKey)) {
            log.warn("⚠️ Token inválido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
        */

        log.info("📱 Webhook WAHA recebido:");
        log.info("Chat ID: {}", dto.chatId());
        log.info("Session: {}", dto.session());

        if (dto.text() != null && !dto.text().isBlank()) {
            log.info("📩 Mensagem: {}", dto.text());

            try {
                // Processar com IA MANTENDO CONTEXTO DA CONVERSA
                String resposta = chatBotService.processarComContexto(
                        dto.chatId(),
                        dto.session(),
                        dto.text()
                );
                log.info("🤖 Resposta gerada pela IA: {}", resposta);

                // Enviar resposta de volta para o WhatsApp via WAHA
                boolean enviado = wahaService.enviarMensagem(
                        dto.session(),
                        dto.chatId(),
                        resposta
                );

                if (enviado) {
                    log.info("✅ Resposta enviada com sucesso para o WhatsApp!");
                    return ResponseEntity.ok().body("Message processed and replied successfully");
                } else {
                    log.error("❌ Falha ao enviar resposta para o WhatsApp");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Message processed but failed to send reply");
                }

            } catch (Exception e) {
                log.error("❌ Erro ao processar mensagem", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error processing message: " + e.getMessage());
            }

        } else {
            log.info("ℹ️ Nenhuma mensagem de texto recebida (pode ser imagem, áudio, etc)");
            return ResponseEntity.ok().body("No text message to process");
        }
    }
}
