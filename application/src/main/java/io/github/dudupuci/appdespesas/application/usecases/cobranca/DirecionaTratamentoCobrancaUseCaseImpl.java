package io.github.dudupuci.appdespesas.application.usecases.cobranca;

import io.github.dudupuci.appdespesas.application.services.strategies.TratadorCobrancaRegistry;
import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasUtils;

public class DirecionaTratamentoCobrancaUseCaseImpl extends DirecionaTratamentoCobrancaUseCase {

    private final TratadorCobrancaRegistry registry;

    public DirecionaTratamentoCobrancaUseCaseImpl(TratadorCobrancaRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void executar(Cobranca cobrancaConfirmada) {
        if (AppDespesasUtils.isEntidadeNotNull(cobrancaConfirmada)) {
            registry.getStrategy(cobrancaConfirmada.getTipoRecursoPago()).tratar(cobrancaConfirmada);
        }
    }
}

