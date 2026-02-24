package io.github.dudupuci.appdespesas.controllers.users.dtos.requests.assinatura;

import java.math.BigDecimal;

public record CheckoutAssinaturaResponseDto(
        Long idAssinatura,
        String nomeAssinatura,
        BigDecimal valor
) {


}
