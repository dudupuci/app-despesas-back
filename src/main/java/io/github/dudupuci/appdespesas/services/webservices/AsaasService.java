package io.github.dudupuci.appdespesas.services.webservices;

import io.github.dudupuci.appdespesas.services.webservices.dtos.request.CriarCobrancaAsaasRequestDto;
import io.github.dudupuci.appdespesas.services.webservices.dtos.request.CriarCustomerAsaasRequestDto;
import io.github.dudupuci.appdespesas.services.webservices.dtos.response.CobrancaCriadaAsaasResponseDto;
import io.github.dudupuci.appdespesas.services.webservices.dtos.response.CustomerCriadoAsaasResponseDto;
import io.github.dudupuci.appdespesas.services.webservices.dtos.response.ObterQrCodePixResponseDto;
import io.github.dudupuci.appdespesas.services.webservices.feignclients.AsaasFeignClient;
import org.springframework.stereotype.Service;

@Service
public class AsaasService {

    private final AsaasFeignClient asaasFeignClient;

    public AsaasService(AsaasFeignClient asaasFeignClient) {
        this.asaasFeignClient = asaasFeignClient;
    }

    public CustomerCriadoAsaasResponseDto criarCustomerAsaas(
            CriarCustomerAsaasRequestDto requestDto
    ) {
        return asaasFeignClient.createCustomer(requestDto);
    }

    public CobrancaCriadaAsaasResponseDto criarCobrancaAsaas(
            CriarCobrancaAsaasRequestDto requestDto
    ) {
        return asaasFeignClient.createCobranca(requestDto);
    }

    public ObterQrCodePixResponseDto obterQrCodePix(String idCobranca) {
        return asaasFeignClient.obterQrCodePix(idCobranca);
    }
}
