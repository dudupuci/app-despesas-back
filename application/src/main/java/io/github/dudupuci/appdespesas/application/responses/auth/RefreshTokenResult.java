package io.github.dudupuci.appdespesas.application.responses.auth;

/**
 * Response interna para o refresh token — sem depender de infra.
 */
public record RefreshTokenResult(
        String accessToken
) {}

