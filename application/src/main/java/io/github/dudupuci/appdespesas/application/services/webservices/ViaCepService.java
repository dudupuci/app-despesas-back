package io.github.dudupuci.appdespesas.application.services.webservices;

import io.github.dudupuci.appdespesas.application.ports.services.ViaCepPort;
import io.github.dudupuci.appdespesas.application.responses.cep.ViaCepResponse;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.ViaCepResponseDto;
import io.github.dudupuci.appdespesas.application.services.webservices.feignclients.ViaCepFeignClient;

public class ViaCepService {

    private final ViaCepPort viaCepPort;

    public ViaCepService(ViaCepPort viaCepPort) {
        this.viaCepPort = viaCepPort;
    }

    public ViaCepResponse buscarEndereco(String cep) {
        return this.viaCepPort.buscarEnderecoPorCep(cep);
    }

}
