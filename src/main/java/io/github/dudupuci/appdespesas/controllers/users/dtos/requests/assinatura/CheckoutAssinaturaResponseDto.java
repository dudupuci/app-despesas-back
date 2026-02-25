package io.github.dudupuci.appdespesas.controllers.users.dtos.requests.assinatura;

import io.github.dudupuci.appdespesas.models.entities.Assinatura;

import java.math.BigDecimal;
import java.util.List;

public record CheckoutAssinaturaResponseDto(
        Long id,
        String nomePlano,
        BigDecimal valor,
        String descricao,
        List<String> beneficios
) {

    public static CheckoutAssinaturaResponseDto fromAssinatura(Assinatura assinatura) {
        return new CheckoutAssinaturaResponseDto(
                assinatura.getId(),
                assinatura.getNomePlano(),
                assinatura.getValor(),
                assinatura.getDescricao() != null ? assinatura.getDescricao() : null,
                assinatura.getBeneficios() != null ? assinatura.getBeneficios() : List.of()
        );
    }
}
