package io.github.dudupuci.appdespesas.controllers.dtos.response.auth;

import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;

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

