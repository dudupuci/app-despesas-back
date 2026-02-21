package io.github.dudupuci.appdespesas.services.webservices.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CustomerCriadoAsaasResponseDto(
        String object,
        String id,
        String dateCreated,
        String name,
        String email,
        String cpfCnpj
) {
}
