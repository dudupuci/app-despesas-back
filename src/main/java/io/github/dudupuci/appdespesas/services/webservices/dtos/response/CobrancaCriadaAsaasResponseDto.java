package io.github.dudupuci.appdespesas.services.webservices.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CobrancaCriadaAsaasResponseDto(
        String object,
        String id,
        String customerId,
        String billingType,
        String status
) {
}
