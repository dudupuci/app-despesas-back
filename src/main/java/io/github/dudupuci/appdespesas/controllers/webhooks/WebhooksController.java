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
            @RequestHeader(value = "X-Webhook-Token") String token,
            @RequestBody WahaMessageRequestDto dto
    ) {

        if (token == null || !token.equals(webhookApiKey)) {
            System.out.println("Token de autenticação ausente ou inválido: " + token);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        if (dto.text() != null && !dto.text().isBlank()) {
            try {
                // Processar com IA MANTENDO CONTEXTO DA CONVERSA
                String resposta = chatBotService.processarComContexto(
                        dto.chatId(),
                        dto.session(),
                        dto.text()
                );
                System.out.println("Resposta gerada pela IA: " + resposta);

                // Enviar resposta de volta para o WhatsApp via WAHA
                boolean enviado = wahaService.enviarMensagem(
                        dto.session(),
                        dto.chatId(),
                        resposta
                );

                if (enviado) {
                    System.out.println("Resposta enviada com sucesso para o WhatsApp");
                    return ResponseEntity.ok().body("Mensagem processada e resposta enviada");
                } else {
                    System.out.println("Falha ao enviar resposta para o WhatsApp");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Mensagem processada, mas falha ao enviar resposta para o WhatsApp");
                }

            } catch (Exception e) {
                System.out.println("Erro ao processar mensagem: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error processing message: " + e.getMessage());
            }

        } else {
            System.out.println("Aguardando mensagem de texto para processar...");
            return ResponseEntity.ok().body("Sem mensagem de texto para processar. Aguardando input do usuário...");
        }
    }
}
