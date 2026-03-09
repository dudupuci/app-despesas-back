package io.github.dudupuci.appdespesas.application.commands.assinatura;

import io.github.dudupuci.appdespesas.domain.enums.BillingType;

/**
 * Command para assinar um plano de assinatura.
 */
public record AssinarAssinaturaCommand(
        String nomeCompleto,
        String email,
        String cpfCnpj,
        boolean assinaturaParaOutraPessoa,
        BillingType formaPagamento
) {}

