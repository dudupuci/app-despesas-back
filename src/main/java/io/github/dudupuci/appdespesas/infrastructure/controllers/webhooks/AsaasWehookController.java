package io.github.dudupuci.appdespesas.infrastructure.controllers.webhooks;

import io.github.dudupuci.appdespesas.infrastructure.controllers.webhooks.dtos.response.AsaasPaymentWebhookResponseDto;
import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.application.usecases.ConfirmarCobrancaUseCase;
import io.github.dudupuci.appdespesas.application.usecases.DirecionaTratamentoCobrancaUseCase;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/asaas")
@Slf4j
public class AsaasWehookController {

    @Value("${integrations.asaas.sandbox.webhook.sandbox.token}")
    private String asaasWebhookToken;

    private final ConfirmarCobrancaUseCase confirmarCobrancaUseCase;
    private final DirecionaTratamentoCobrancaUseCase direcionaTratamentoCobrancaUseCase;

    public AsaasWehookController(ConfirmarCobrancaUseCase confirmarCobrancaUseCase, DirecionaTratamentoCobrancaUseCase direcionaTratamentoCobrancaUseCase) {
        this.confirmarCobrancaUseCase = confirmarCobrancaUseCase;
        this.direcionaTratamentoCobrancaUseCase = direcionaTratamentoCobrancaUseCase;
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

        if ("PAYMENT_RECEIVED".equals(payload.event()) && payload.payment().id() != null) {

            String asaasCobrancaId = payload.payment().id();
            String paymentDate = payload.payment().paymentDate();

            Date dataPagamento = AppDespesasUtils.converterDataFromStringAnoMesDia(paymentDate);

            Cobranca cobrancaConfirmada = this.confirmarCobrancaUseCase.executar(
                    dataPagamento, asaasCobrancaId
            );

            this.direcionaTratamentoCobrancaUseCase.executar(cobrancaConfirmada);

        }

        return ResponseEntity.ok(payload);
    }
}
