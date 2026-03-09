package io.github.dudupuci.appdespesas.infrastructure.utils;

import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/**
 * Utilitário de segurança — fica na infrastructure pois usa Spring Security.
 * Usado pelos controllers para obter o usuário autenticado do contexto.
 */
public final class SecurityUtils {

    private SecurityUtils() {}

    public static UsuarioSistema getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UsuarioSistema usuario) {
            return usuario;
        }
        return null;
    }

    public static UUID getUsuarioAutenticadoId() {
        UsuarioSistema usuario = getUsuarioAutenticado();
        return usuario != null ? usuario.getId() : null;
    }

    public static String getUsuarioAutenticadoEmail() {
        UsuarioSistema usuario = getUsuarioAutenticado();
        return usuario != null ? usuario.getContato().getEmail() : null;
    }
}

