package io.github.dudupuci.appdespesas.application.usecases;

import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.application.services.CobrancaService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ConfirmarCobrancaUseCase {

    private final CobrancaService cobrancaService;

    public ConfirmarCobrancaUseCase(CobrancaService cobrancaService) {
        this.cobrancaService = cobrancaService;
    }

    public Cobranca executar(
            Date dataPagamento,
            String asaasPaymentId
    ) {
        return this.cobrancaService.atualizaCobrancaConfirmada(dataPagamento, asaasPaymentId);
    }

}
