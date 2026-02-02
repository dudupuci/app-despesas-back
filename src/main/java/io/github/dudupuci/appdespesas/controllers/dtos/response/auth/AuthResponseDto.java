package io.github.dudupuci.appdespesas.controllers.dtos.response.auth;

import io.github.dudupuci.appdespesas.controllers.dtos.base.ResponseDto;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;
import lombok.Getter;

@Getter
public class AuthResponseDto extends ResponseDto {
    private final String accessToken;
    private final String refreshToken;
    private final String tipo;
    private final UsuarioInfoDto usuario;

    public AuthResponseDto(
            String accessToken,
            String refreshToken,
            String tipo,
            UsuarioInfoDto usuario,
            String mensagemKey
    ) {
        super(AppDespesasMessages.getMessage(mensagemKey));
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.tipo = tipo;
        this.usuario = usuario;
    }

    public static AuthResponseDto fromEntityLogin(UsuarioSistema usuarioSistema, String accessToken, String refreshToken) {
        return new AuthResponseDto(
                accessToken,
                refreshToken,
                "Bearer",
                UsuarioInfoDto.fromEntity(usuarioSistema),
                "auth.login.sucesso"
        );
    }

    public static AuthResponseDto fromEntityRegistro(UsuarioSistema usuarioSistema, String accessToken, String refreshToken) {
        return new AuthResponseDto(
                accessToken,
                refreshToken,
                "Bearer",
                UsuarioInfoDto.fromEntity(usuarioSistema),
                "auth.registro.sucesso"
        );
    }
}

