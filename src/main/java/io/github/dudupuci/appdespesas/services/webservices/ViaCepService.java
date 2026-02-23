package io.github.dudupuci.appdespesas.services.webservices;


import io.github.dudupuci.appdespesas.controllers.noauth.dtos.response.cep.ViaCepResponseDto;
import io.github.dudupuci.appdespesas.services.webservices.feignclients.ViaCepFeignClient;
import org.springframework.stereotype.Service;

@Service
public class ViaCepService {

    private final ViaCepFeignClient viaCepFeignClient;

    public ViaCepService(ViaCepFeignClient viaCepFeignClient) {
        this.viaCepFeignClient = viaCepFeignClient;
    }

    public ViaCepResponseDto buscarEndereco(String cep) {
        return viaCepFeignClient.buscarEnderecoPorCep(cep);
    }

}
