package io.github.dudupuci.appdespesas.application.ports.services;

import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;

/**
 * Port para geração e validação de tokens JWT.
 * Permite que o AuthService não dependa da implementação concreta da infra.
 */
public interface JwtPort {

    String generateAccessToken(UsuarioSistema usuario);

    String generateRefreshToken(UsuarioSistema usuario);

    String extractEmail(String token);

    boolean isTokenValid(String token);
}

