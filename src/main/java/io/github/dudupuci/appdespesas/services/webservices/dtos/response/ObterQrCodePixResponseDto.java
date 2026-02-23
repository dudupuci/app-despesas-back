package io.github.dudupuci.appdespesas.services.webservices.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ObterQrCodePixResponseDto(
        boolean success,
        String encodedImage,
        String payload,
        String expirationDate,
        String description
) {
}
