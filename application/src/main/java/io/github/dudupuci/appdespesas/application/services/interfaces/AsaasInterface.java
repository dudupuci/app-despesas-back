package io.github.dudupuci.appdespesas.application.services.interfaces;

import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.CriarCobrancaAsaasRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.CriarCustomerAsaasRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.CobrancaCriadaAsaasResponseDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.CustomerCriadoAsaasResponseDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.ObterQrCodePixResponseDto;

/**
 * Port para integração com o gateway de pagamentos Asaas.
 */
public interface AsaasPort {
    CustomerCriadoAsaasResponseDto createCustomer(CriarCustomerAsaasRequestDto requestDto);
    CobrancaCriadaAsaasResponseDto createCobranca(CriarCobrancaAsaasRequestDto requestDto);
    ObterQrCodePixResponseDto obterQrCodePix(String cobrancaId);
}

