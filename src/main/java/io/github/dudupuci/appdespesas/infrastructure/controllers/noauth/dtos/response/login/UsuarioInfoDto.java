package io.github.dudupuci.appdespesas.infrastructure.controllers.noauth.dtos.response.login;

import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;

public record UsuarioInfoDto(
        String nome,
        String sobrenome,
        String celular,
        String nomeUsuario,
        String email,
        String role
) {
    public static UsuarioInfoDto fromEntity(UsuarioSistema usuario) {
        return new UsuarioInfoDto(
                usuario.getNome(),
                usuario.getSobrenome(),
                usuario.getContato().getCelular(),
                usuario.getNomeUsuario(),
                usuario.getContato().getEmail(),
                usuario.getRole().getNome()
        );
    }
}

