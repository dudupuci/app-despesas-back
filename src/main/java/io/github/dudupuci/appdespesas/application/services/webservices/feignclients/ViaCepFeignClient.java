package io.github.dudupuci.appdespesas.application.services.webservices.feignclients;

import io.github.dudupuci.appdespesas.infrastructure.controllers.noauth.dtos.response.cep.ViaCepResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "viaCepFeignClient",
        url = "${integrations.viacep.baseUrl}"
)
public interface ViaCepFeignClient {

    @GetMapping("/{cep}/json/")
    ViaCepResponseDto buscarEnderecoPorCep(@PathVariable String cep);

}
