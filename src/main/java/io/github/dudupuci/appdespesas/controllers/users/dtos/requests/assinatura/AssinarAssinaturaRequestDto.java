package io.github.dudupuci.appdespesas.controllers.users.dtos.requests.assinatura;


import io.github.dudupuci.appdespesas.models.enums.FrequenciaRecorrencia;

public record AssinarAssinaturaRequestDto(
        String nomePlano,
        FrequenciaRecorrencia frequenciaRecorrencia
) {
}
