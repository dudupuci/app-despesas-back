package io.github.dudupuci.appdespesas.application.responses.auth;

/**
 * Response de autenticação retornado pelo AuthService.
 * Contém os tokens gerados e dados básicos do usuário.
 */
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String nomeUsuario,
        String email,
        String role
) {}

