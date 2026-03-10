package io.github.dudupuci.appdespesas.application.usecases.cobranca;

import io.github.dudupuci.appdespesas.application.usecases.base.UseCase;
import io.github.dudupuci.appdespesas.domain.entities.Cobranca;

import java.util.Date;

public abstract class ConfirmarCobrancaUseCase extends UseCase<ConfirmarCobrancaUseCase.Command, Cobranca> {
    public record Command(Date dataPagamento, String asaasPaymentId) {}
}

