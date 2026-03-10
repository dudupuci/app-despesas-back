package io.github.dudupuci.appdespesas.application.usecases.base;

/**
 * Command genérico para operações de deleção de entidades com ID Long e verificação de ownership.
 */
public record DeleteLongCommand(java.util.UUID usuarioId, Long id) {}

