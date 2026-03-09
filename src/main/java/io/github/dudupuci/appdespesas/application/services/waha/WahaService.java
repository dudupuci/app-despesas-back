package io.github.dudupuci.appdespesas.application.services.waha;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

/**
 * Service para enviar mensagens de volta para o WhatsApp via WAHA API
 */
@Service
@Slf4j
public class WahaService {

    private final RestTemplate restTemplate;

    @Value("${waha.api.url:http://localhost:3001}")
    private String wahaApiUrl;

    @Value("${waha.api.key:}")
    private String wahaApiKey;

    @Value("${app.webhook.url:http://localhost:8080}") // URL do SEU backend
    private String backendUrl;

    public WahaService() {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Envia uma mensagem de texto para o WhatsApp via WAHA
     *
     * @param session Nome da sessão do WAHA
     * @param chatId  ID do chat (ex: 5511999999999@c.us)
     * @param text    Texto da mensagem
     * @return true se enviado com sucesso, false caso contrário
     */
    public boolean enviarMensagem(String session, String chatId, String text) {
        try {
            String url = wahaApiUrl + "/api/sendText";

            Map<String, String> payload = Map.of(
                    "session", session,
                    "chatId", chatId,
                    "text", text
            );

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Adicionar X-Api-Key se configurado
            if (wahaApiKey != null && !wahaApiKey.isBlank()) {
                headers.set("X-Api-Key", wahaApiKey);
                log.debug("🔑 Usando X-Api-Key para autenticar no WAHA");
            }

            HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

            log.info("📤 Enviando mensagem para WAHA: session={}, chatId={}", session, chatId);
            log.debug("🔍 URL: {}", url);
            log.debug("🔍 Payload: {}", payload);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("✅ Mensagem enviada com sucesso para o WhatsApp");
                return true;
            } else {
                log.warn("⚠️ Resposta inesperada do WAHA: {}", response.getStatusCode());
                return false;
            }

        } catch (Exception e) {
            log.error("❌ Erro ao enviar mensagem para o WAHA", e);
            return false;
        }
    }

}

