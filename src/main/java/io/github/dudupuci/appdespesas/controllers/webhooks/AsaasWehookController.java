package io.github.dudupuci.appdespesas.controllers.webhooks;

import io.github.dudupuci.appdespesas.controllers.webhooks.dtos.response.AsaasPaymentWebhookResponseDto;
import io.github.dudupuci.appdespesas.models.entities.Cobranca;
import io.github.dudupuci.appdespesas.services.AssinaturaService;
import io.github.dudupuci.appdespesas.services.CobrancaService;
import io.github.dudupuci.appdespesas.services.UsuarioService;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/asaas")
@Slf4j
public class AsaasWehookController {

    @Value("${integrations.asaas.sandbox.webhook.sandbox.token}")
    private String asaasWebhookToken;

    private final CobrancaService cobrancaService;

    public AsaasWehookController(CobrancaService cobrancaService, AssinaturaService assinaturaService, UsuarioService usuarioService) {
        this.cobrancaService = cobrancaService;
    }

    @PostMapping("/webhook")
    public ResponseEntity<AsaasPaymentWebhookResponseDto> handleAsaasPostWebhook(
            @RequestHeader(value = "Asaas-Access-Token", required = false) String token,
            @RequestBody AsaasPaymentWebhookResponseDto payload
    ) {

        if (token == null || !token.equals(asaasWebhookToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (payload == null || payload.event() == null || payload.payment() == null) {
            return ResponseEntity.badRequest().build();
        }

        System.out.println("=== DEBUG ASAAAS WEBHOOK ===");
        System.out.println("Event: " + payload.event());
        System.out.println("Payment ID: " + payload.payment().id());
        System.out.println("Status: " + payload.payment().status());
        System.out.println("=== FIM DEBUG ASAAAS WEBHOOK ===");

        if ("PAYMENT_CONFIRMED".equals(payload.event())
                && payload.payment().id() != null) {

            String asaasPaymentId = payload.payment().id();
            String paymentDate = payload.payment().paymentDate();

            Date dataPagamento = AppDespesasUtils.converterDataFromStringAnoMesDia(paymentDate);

            Cobranca cobrancaConfirmadaAsaas = this.cobrancaService.buscarPorAsaasCobrancaId(asaasPaymentId);
            this.cobrancaService.atualizaCobrancaConfirmada(dataPagamento, cobrancaConfirmadaAsaas);
        }

        return ResponseEntity.ok(payload);
    }
}
