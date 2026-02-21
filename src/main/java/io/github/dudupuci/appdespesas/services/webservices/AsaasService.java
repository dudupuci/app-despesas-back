package io.github.dudupuci.appdespesas.services.webservices;

import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.services.UsuarioService;
import io.github.dudupuci.appdespesas.services.webservices.dtos.request.CriarCustomerAsaasRequestDto;
import io.github.dudupuci.appdespesas.services.webservices.dtos.response.CustomerCriadoAsaasResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AsaasService {

    private final UsuarioService usuarioService;

    public AsaasService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Value("${integrations.asaas.sandbox.customers}")
    private String asaasCustomersEndpoint;

    @Value("${integrations.asaas.sandbox.apiKey}")
    private String asaasApiKey;


    public CustomerCriadoAsaasResponseDto criarCustomerAsaas(CriarCustomerAsaasRequestDto customerDto) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("access_token", asaasApiKey);

        HttpEntity<CriarCustomerAsaasRequestDto> request = new HttpEntity<>(customerDto, headers);

        ResponseEntity<CustomerCriadoAsaasResponseDto> response = restTemplate.exchange(
                asaasCustomersEndpoint,
                HttpMethod.POST,
                request,
                CustomerCriadoAsaasResponseDto.class
        );

        CustomerCriadoAsaasResponseDto customerCriadoDto = response.getBody();

        if (response.getStatusCode().is2xxSuccessful() && customerCriadoDto != null) {
            return customerCriadoDto;
        } else {
            throw new RuntimeException("Erro ao criar customer no Asaas: " + response.getStatusCode());
        }

    }


}
