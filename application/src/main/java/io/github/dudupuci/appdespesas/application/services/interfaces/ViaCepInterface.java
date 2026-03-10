package io.github.dudupuci.appdespesas.application.services.interfaces;

import io.github.dudupuci.appdespesas.application.responses.cep.ViaCepResponse;

public interface ViaCepPort {
    ViaCepResponse buscarEnderecoPorCep(String cep);
}
