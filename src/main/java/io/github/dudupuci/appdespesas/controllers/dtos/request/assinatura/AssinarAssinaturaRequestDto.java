package io.github.dudupuci.appdespesas.controllers.dtos.request.assinatura;


import io.github.dudupuci.appdespesas.models.enums.FrequenciaRecorrencia;

public record AssinarAssinaturaRequestDto(
        String nomePlano,
        FrequenciaRecorrencia frequenciaRecorrencia
) {
}
