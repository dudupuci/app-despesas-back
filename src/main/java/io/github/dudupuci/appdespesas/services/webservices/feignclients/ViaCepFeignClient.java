package io.github.dudupuci.appdespesas.services.webservices.feignclients;

import io.github.dudupuci.appdespesas.config.openfeign.AsaasFeignConfig;
import io.github.dudupuci.appdespesas.controllers.noauth.dtos.response.cep.ViaCepResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "viaCepClient",
        url = "${integrations.viacep.baseUrl}"
)
public interface ViaCepFeignClient {

    @GetMapping("/{cep}/json/")
    ViaCepResponseDto buscarEnderecoPorCep(@PathVariable String cep);

}
