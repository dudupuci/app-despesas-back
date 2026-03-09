package io.github.dudupuci.appdespesas.application.services.webservices.feignclients;

import io.github.dudupuci.appdespesas.application.responses.cep.ViaCepResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "viaCepFeignClient",
        url = "${integrations.viacep.baseUrl}"
)
public interface ViaCepFeignClient {

    @GetMapping("/{cep}/json/")
    ViaCepResponse buscarEnderecoPorCep(@PathVariable String cep);
}
