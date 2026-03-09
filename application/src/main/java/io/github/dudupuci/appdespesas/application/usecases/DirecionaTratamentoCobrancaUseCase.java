package io.github.dudupuci.appdespesas.application.usecases;

import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.application.services.strategies.TratadorCobrancaRegistry;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasUtils;
import org.springframework.stereotype.Service;

/**
 * Use case responsável por direcionar o tratamento da cobrança confirmada para a estratégia correta,
 * de acordo com o tipo de recurso pago.
 * <br/>
 * Exemplo: se a cobrança confirmada for referente a uma assinatura,
 * o use case irá direcionar o tratamento para a estratégia de
 * tratamento de cobranças de assinaturas, que irá realizar as
 */
@Service
public class DirecionaTratamentoCobrancaUseCase {

    private final TratadorCobrancaRegistry registry;

    public DirecionaTratamentoCobrancaUseCase(TratadorCobrancaRegistry registry) {
        this.registry = registry;
    }

    public void executar(
            Cobranca cobrancaConfirmada
    ) {
        if (AppDespesasUtils.isEntidadeNotNull(cobrancaConfirmada)) {
            this.registry
                    .getStrategy(cobrancaConfirmada.getTipoRecursoPago())
                    .tratar(cobrancaConfirmada);
        }
    }

}
