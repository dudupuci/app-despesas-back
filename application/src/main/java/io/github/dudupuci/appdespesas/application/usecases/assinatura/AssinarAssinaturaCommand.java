package io.github.dudupuci.appdespesas.application.usecases.assinatura;

import io.github.dudupuci.appdespesas.domain.enums.BillingType;

import java.util.UUID;

/**
 * Command para assinar um plano de assinatura.
 */
public record AssinarAssinaturaCommand(
        UUID usuarioIdLogado,
        Long assinaturaId,
        String nomeCompleto,
        String email,
        String cpfCnpj,
        boolean assinaturaParaOutraPessoa,
        BillingType formaPagamento
) {}
