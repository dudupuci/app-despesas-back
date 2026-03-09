package io.github.dudupuci.appdespesas.application.commands.auth;

/**
 * Command para realizar login no sistema.
 */
public record LoginCommand(
        String email,
        String senha
) {}

