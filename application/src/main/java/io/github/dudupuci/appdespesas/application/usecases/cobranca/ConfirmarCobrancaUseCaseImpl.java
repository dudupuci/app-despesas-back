package io.github.dudupuci.appdespesas.application.usecases.cobranca;

import io.github.dudupuci.appdespesas.application.services.CobrancaService;
import io.github.dudupuci.appdespesas.domain.entities.Cobranca;

public class ConfirmarCobrancaUseCaseImpl extends ConfirmarCobrancaUseCase {

    private final CobrancaService cobrancaService;

    public ConfirmarCobrancaUseCaseImpl(CobrancaService cobrancaService) {
        this.cobrancaService = cobrancaService;
    }

    @Override
    public Cobranca executar(Command cmd) {
        return cobrancaService.atualizaCobrancaConfirmada(cmd.dataPagamento(), cmd.asaasPaymentId());
    }
}

