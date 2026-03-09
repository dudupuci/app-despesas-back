package io.github.dudupuci.appdespesas.application.commands.auth;

/**
 * Command para realizar login.
 */
public record LoginCommand(
        String email,
        String senha
) {}

