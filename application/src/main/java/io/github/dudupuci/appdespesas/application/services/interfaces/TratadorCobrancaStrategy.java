package io.github.dudupuci.appdespesas.application.services.interfaces;

import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.domain.enums.TipoRecursoPago;

public interface TratadorCobrancaStrategy {
    TipoRecursoPago getTipoSuportado();
    void tratar(Cobranca cobranca);

}
