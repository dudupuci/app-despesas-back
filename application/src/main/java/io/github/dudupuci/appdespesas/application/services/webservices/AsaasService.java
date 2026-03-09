package io.github.dudupuci.appdespesas.application.services.webservices;

import io.github.dudupuci.appdespesas.application.ports.services.AsaasPort;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.CriarCobrancaAsaasRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.CriarCustomerAsaasRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.CobrancaCriadaAsaasResponseDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.CustomerCriadoAsaasResponseDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.ObterQrCodePixResponseDto;
import org.springframework.stereotype.Service;

@Service
public class AsaasService {

    private final AsaasPort asaasPort;

    public AsaasService(AsaasPort asaasPort) {
        this.asaasPort = asaasPort;
    }

    public CustomerCriadoAsaasResponseDto criarCustomerAsaas(
            CriarCustomerAsaasRequestDto requestDto
    ) {
        return asaasPort.createCustomer(requestDto);
    }

    public CobrancaCriadaAsaasResponseDto criarCobrancaAsaas(
            CriarCobrancaAsaasRequestDto requestDto
    ) {
        return asaasPort.createCobranca(requestDto);
    }

    public ObterQrCodePixResponseDto obterQrCodePix(String idCobranca) {
        return asaasPort.obterQrCodePix(idCobranca);
    }
}
