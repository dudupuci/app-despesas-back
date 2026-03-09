package io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.assinatura;

import io.github.dudupuci.appdespesas.application.responses.assinatura.CheckoutAssinaturaResult;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;

import java.math.BigDecimal;
import java.util.List;

public record CheckoutAssinaturaResponseDto(
        Long id,
        String nomePlano,
        BigDecimal valor,
        String descricao,
        List<String> beneficios
) {
    public static CheckoutAssinaturaResponseDto fromResult(CheckoutAssinaturaResult result) {
        return new CheckoutAssinaturaResponseDto(
                result.id(),
                result.nomePlano(),
                result.valor(),
                result.descricao(),
                result.beneficios()
        );
    }

    /** @deprecated use fromResult() */
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
