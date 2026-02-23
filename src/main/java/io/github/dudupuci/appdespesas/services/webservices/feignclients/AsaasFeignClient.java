package io.github.dudupuci.appdespesas.services.webservices.feignclients;

import io.github.dudupuci.appdespesas.config.openfeign.AsaasFeignConfig;
import io.github.dudupuci.appdespesas.services.webservices.dtos.request.CriarCobrancaAsaasRequestDto;
import io.github.dudupuci.appdespesas.services.webservices.dtos.request.CriarCustomerAsaasRequestDto;
import io.github.dudupuci.appdespesas.services.webservices.dtos.response.CobrancaCriadaAsaasResponseDto;
import io.github.dudupuci.appdespesas.services.webservices.dtos.response.CustomerCriadoAsaasResponseDto;
import io.github.dudupuci.appdespesas.services.webservices.dtos.response.ObterQrCodePixResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign Client para integração com o Asaas.
 * Este cliente é configurado para se comunicar com a API do Asaas,
 * utilizando as URLs e endpoints definidos nas propriedades de configuração.
 * <br/>
 * <br/>
 * AsaasFeignConfig é a classe de configuração personalizada para este cliente,
 * onde podem ser definidos interceptadores, decodificadores, etc.
 */
@FeignClient(name = "asaasClient",
        url = "${integrations.asaas.sandbox.baseUrl}",
        configuration = AsaasFeignConfig.class
)
public interface AsaasFeignClient {

    @PostMapping("${integrations.asaas.sandbox.customers}")
    CustomerCriadoAsaasResponseDto createCustomer(
            @RequestBody CriarCustomerAsaasRequestDto requestDto
    );

    @PostMapping("${integrations.asaas.sandbox.cobrancas}")
    CobrancaCriadaAsaasResponseDto createCobranca(
            @RequestBody CriarCobrancaAsaasRequestDto requestDto
    );

    @GetMapping("${integrations.asaas.sandbox.obterQrCodePix}/{id}/pixQrCode")
    ObterQrCodePixResponseDto obterQrCodePix(@PathVariable String id);



}
