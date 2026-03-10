package io.github.dudupuci.appdespesas.application.usecases.assinatura;

import java.util.UUID;

public record PrepararAssinaturaCommand(UUID usuarioId, Long assinaturaId) {}

