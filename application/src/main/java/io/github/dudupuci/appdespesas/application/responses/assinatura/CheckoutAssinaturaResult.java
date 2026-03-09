package io.github.dudupuci.appdespesas.application.responses.assinatura;

import io.github.dudupuci.appdespesas.domain.entities.Assinatura;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response do PrepararAssinaturaPlanoUseCase — sem depender de infra.
 */
public record CheckoutAssinaturaResult(
        Long id,
        String nomePlano,
        BigDecimal valor,
        String descricao,
        List<String> beneficios
) {
    public static CheckoutAssinaturaResult fromAssinatura(Assinatura assinatura) {
        return new CheckoutAssinaturaResult(
                assinatura.getId(),
                assinatura.getNomePlano(),
                assinatura.getValor(),
                assinatura.getDescricao() != null ? assinatura.getDescricao() : null,
                assinatura.getBeneficios() != null ? assinatura.getBeneficios() : List.of()
        );
    }
}

