package io.github.dudupuci.appdespesas.config.app.jwt;

import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.repositories.UsuariosRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final UsuariosRepository usuariosRepository;

    public JwtAuthenticationFilter(JwtConfig jwtConfig,
                                   UsuariosRepository usuariosRepository) {
        this.jwtConfig = jwtConfig;
        this.usuariosRepository = usuariosRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        String email;

        try {
            email = jwtConfig.extractEmail(token);
        } catch (Exception ex) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {

            Optional<UsuarioSistema> usuarioOptional = usuariosRepository.buscarPorEmail(email);

            if (usuarioOptional.isPresent()) {
                UsernamePasswordAuthenticationToken authentication = getUsernamePasswordAuthenticationToken(usuarioOptional);

                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private static UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(Optional<UsuarioSistema> usuarioOptional) {
        assert usuarioOptional.isPresent() : "Usuário deve estar presente para criar AuthenticationToken";
        UsuarioSistema usuarioSistema = usuarioOptional.get();

        var authority = new SimpleGrantedAuthority(
                "ROLE_" + usuarioSistema.getRole().getNome()
        );

        return new UsernamePasswordAuthenticationToken(
                usuarioSistema,  // Passa o objeto UsuarioSistema, não Optional
                null,
                List.of(authority)
        );
    }
}
