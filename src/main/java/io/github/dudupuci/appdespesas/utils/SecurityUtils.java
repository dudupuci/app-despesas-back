package io.github.dudupuci.appdespesas.utils;

import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityUtils {

    /**
     * Obtém o usuário autenticado do contexto de segurança
     * @return UsuarioSistema autenticado
     */
    public static UsuarioSistema getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UsuarioSistema) {
            return (UsuarioSistema) principal;
        }

        return null;
    }

    /**
     * Obtém o ID do usuário autenticado
     * @return ID do usuário ou null se não autenticado
     */
    public static UUID getUsuarioAutenticadoId() {
        UsuarioSistema usuario = getUsuarioAutenticado();
        return usuario != null ? usuario.getId() : null;
    }

    /**
     * Obtém o email do usuário autenticado
     * @return Email do usuário ou null se não autenticado
     */
    public static String getUsuarioAutenticadoEmail() {
        UsuarioSistema usuario = getUsuarioAutenticado();
        return usuario != null ? usuario.getContato().getEmail() : null;
    }
}

