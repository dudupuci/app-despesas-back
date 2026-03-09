package io.github.dudupuci.appdespesas.application.responses.auth;

import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;

/**
 * Response interna do AuthService — sem depender de nenhuma camada de infra.
 */
public record AuthResult(
        String accessToken,
        String refreshToken,
        UsuarioSistema usuario
) {}

