package io.github.dudupuci.appdespesas.application.services.webservices;

import io.github.dudupuci.appdespesas.application.responses.cep.ViaCepResponse;
import io.github.dudupuci.appdespesas.application.services.webservices.feignclients.ViaCepFeignClient;
import org.springframework.stereotype.Service;

public class ViaCepService {

    private final ViaCepFeignClient viaCepFeignClient;

    public ViaCepService(ViaCepFeignClient viaCepFeignClient) {
        this.viaCepFeignClient = viaCepFeignClient;
    }

    public ViaCepResponse buscarEndereco(String cep) {
        return viaCepFeignClient.buscarEnderecoPorCep(cep);
    }

}
