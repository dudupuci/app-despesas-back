package io.github.dudupuci.appdespesas.controllers.webhooks;

import io.github.dudupuci.appdespesas.controllers.webhooks.dtos.request.WahaWebhookDto;
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
            @RequestHeader(value = "X-Webhook-Token") String token,
            @RequestBody WahaWebhookDto dto
    ) {
        System.out.println("=== DEBUG WEBHOOK ===");
        System.out.println("Event: " + dto.event());
        System.out.println("From: " + dto.payload().from());
        System.out.println("To: " + dto.payload().to());
        System.out.println("Body: " + dto.payload().body());
        System.out.println("FromMe: " + dto.payload().fromMe());
        System.out.println("=== FIM DEBUG ===");

        if (token == null || !token.equals(webhookApiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        // ⚠️ IGNORAR mensagens SUAS
        if (dto.payload().fromMe()) {
            return ResponseEntity.ok("Mensagem própria ignorada");
        }

        // ⚠️ IGNORAR mensagens de CANAIS (newsletter)
        if (dto.payload().from().contains("@newsletter")) {
            return ResponseEntity.ok("Mensagem de canal ignorada");
        }

        if (dto.payload().body() != null && !dto.payload().body().isBlank()) {
            try {
                String resposta = chatBotService.processarComContexto(
                        dto.payload().from(),  // chatId
                        dto.session(),
                        dto.payload().body()   // texto
                );

                boolean enviado = wahaService.enviarMensagem(
                        dto.session(),
                        dto.payload().from(),
                        resposta
                );

                return enviado ?
                        ResponseEntity.ok("Resposta enviada") :
                        ResponseEntity.status(500).body("Falha ao enviar");

            } catch (Exception e) {
                return ResponseEntity.status(500).body("Erro: " + e.getMessage());
            }
        }

        return ResponseEntity.ok("Sem mensagem de texto");
    }



}
