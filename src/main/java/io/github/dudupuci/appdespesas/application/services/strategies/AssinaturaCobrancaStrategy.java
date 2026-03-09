package io.github.dudupuci.appdespesas.application.services.strategies;

import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.domain.enums.TipoRecursoPago;
import io.github.dudupuci.appdespesas.application.services.interfaces.TratadorCobrancaStrategy;
import io.github.dudupuci.appdespesas.application.usecases.TratarCobrancaUseCase;
import org.springframework.stereotype.Service;

@Service
public class AssinaturaCobrancaStrategy implements TratadorCobrancaStrategy {

    private final TratarCobrancaUseCase tratarCobrancaUseCase;

    public AssinaturaCobrancaStrategy(TratarCobrancaUseCase tratarCobrancaUseCase) {
        this.tratarCobrancaUseCase = tratarCobrancaUseCase;
    }

    @Override
    public TipoRecursoPago getTipoSuportado() {
        return TipoRecursoPago.ASSINATURA;
    }

    @Override
    public void tratar(Cobranca cobranca) {
        tratarCobrancaUseCase.executar(cobranca);
    }
}