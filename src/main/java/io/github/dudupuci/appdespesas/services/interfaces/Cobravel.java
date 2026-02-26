package io.github.dudupuci.appdespesas.services.interfaces;

import io.github.dudupuci.appdespesas.models.entities.Cobranca;
import io.github.dudupuci.appdespesas.models.entities.base.Entidade;

public interface Cobravel {
    void tratarCobrancaConfirmada(Cobranca cobranca);
}
