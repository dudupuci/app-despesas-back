package io.github.dudupuci.appdespesas.application.responses.cep;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Response do serviço ViaCep.
 * Fica no application pois é usado pelo ViaCepService e ViaCepFeignClient.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepResponse(
        String cep,
        String logradouro,
        String complemento,
        String bairro,
        String localidade,
        String uf,
        String estado
) {}

