package io.github.dudupuci.appdespesas.infrastructure.adapters.feignclients;

import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.CriarCobrancaAsaasRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.CriarCustomerAsaasRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.CobrancaCriadaAsaasResponseDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.CustomerCriadoAsaasResponseDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.ObterQrCodePixResponseDto;
import io.github.dudupuci.appdespesas.infrastructure.config.openfeign.AsaasFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign Client para integração com o Asaas — fica na infra pois depende de AsaasFeignConfig.
 */
@FeignClient(
        name = "asaasFeignClient",
        url = "${integrations.asaas.sandbox.baseUrl}",
        configuration = AsaasFeignConfig.class
)
public interface AsaasFeignClientAdapter {

    @PostMapping("${integrations.asaas.sandbox.customers}")
    CustomerCriadoAsaasResponseDto createCustomer(@RequestBody CriarCustomerAsaasRequestDto requestDto);

    @PostMapping("${integrations.asaas.sandbox.cobrancas}")
    CobrancaCriadaAsaasResponseDto createCobranca(@RequestBody CriarCobrancaAsaasRequestDto requestDto);

    @GetMapping("${integrations.asaas.sandbox.obterQrCodePix}/{id}/pixQrCode")
    ObterQrCodePixResponseDto obterQrCodePix(@PathVariable String id);
}

