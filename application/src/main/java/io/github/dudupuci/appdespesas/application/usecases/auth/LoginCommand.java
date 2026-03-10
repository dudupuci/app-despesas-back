package io.github.dudupuci.appdespesas.application.usecases.auth;

/**
 * Command para realizar login.
 */
public record LoginCommand(
        String email,
        String senha
) {}

