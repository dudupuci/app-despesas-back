package io.github.dudupuci.appdespesas.services.webservices.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ObterQrCodePixResponseDto(
        boolean success,
        String encodedImage,
        String payload,
        String expirationDate,
        String description,
        UUID usuarioBeneficiarioId
) {
}
