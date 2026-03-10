package io.github.dudupuci.appdespesas.infrastructure.adapters.feignclients.viacep;

import io.github.dudupuci.appdespesas.application.responses.cep.ViaCepResponse;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.ViaCepResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "viaCepFeignClient",
        url = "${integrations.viacep.baseUrl}"
)
public interface ViaCepFeignClientAdapter {

    @GetMapping("/{cep}/json/")
    ViaCepResponse buscarEnderecoPorCep(@PathVariable String cep);

}
