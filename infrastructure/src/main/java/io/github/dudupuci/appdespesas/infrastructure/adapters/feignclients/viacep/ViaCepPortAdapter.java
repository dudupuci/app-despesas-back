package io.github.dudupuci.appdespesas.infrastructure.adapters.feignclients.viacep;

import io.github.dudupuci.appdespesas.application.ports.services.ViaCepPort;
import io.github.dudupuci.appdespesas.application.responses.cep.ViaCepResponse;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.ViaCepResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViaCepPortAdapter implements ViaCepPort {

    private final ViaCepFeignClientAdapter feignClientAdapter;

    @Override
    public ViaCepResponse buscarEnderecoPorCep(String cep) {
        return this.feignClientAdapter.buscarEnderecoPorCep(cep);
    }
}
