package io.github.dudupuci.appdespesas.application.usecases.base;

import java.util.UUID;

/**
 * Command genérico para operações de deleção que precisam verificar ownership.
 * Contém o ID do recurso e o ID do usuário autenticado.
 */
public record DeleteCommand(UUID usuarioId, UUID id) {}

