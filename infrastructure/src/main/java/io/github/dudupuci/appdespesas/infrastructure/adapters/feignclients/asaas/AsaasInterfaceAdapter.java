package io.github.dudupuci.appdespesas.infrastructure.adapters.feignclients.asaas;

import io.github.dudupuci.appdespesas.application.services.interfaces.AsaasPort;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.CriarCobrancaAsaasRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.CriarCustomerAsaasRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.CobrancaCriadaAsaasResponseDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.CustomerCriadoAsaasResponseDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.ObterQrCodePixResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Adapter que implementa AsaasPort usando o AsaasFeignClientAdapter.
 */
@Component
@RequiredArgsConstructor
public class AsaasPortAdapter implements AsaasPort {

    private final AsaasFeignClientAdapter feignClient;

    @Override
    public CustomerCriadoAsaasResponseDto createCustomer(CriarCustomerAsaasRequestDto requestDto) {
        return feignClient.createCustomer(requestDto);
    }

    @Override
    public CobrancaCriadaAsaasResponseDto createCobranca(CriarCobrancaAsaasRequestDto requestDto) {
        return feignClient.createCobranca(requestDto);
    }

    @Override
    public ObterQrCodePixResponseDto obterQrCodePix(String cobrancaId) {
        return feignClient.obterQrCodePix(cobrancaId);
    }
}

