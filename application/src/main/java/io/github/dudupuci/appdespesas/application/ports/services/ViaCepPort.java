package io.github.dudupuci.appdespesas.application.ports.services;

import io.github.dudupuci.appdespesas.application.responses.cep.ViaCepResponse;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.ViaCepResponseDto;

public interface ViaCepPort {
    ViaCepResponse buscarEnderecoPorCep(String cep);
}
