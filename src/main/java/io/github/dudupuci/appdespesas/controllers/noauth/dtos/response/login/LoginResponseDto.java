package io.github.dudupuci.appdespesas.controllers.noauth.dtos.response.login;

import io.github.dudupuci.appdespesas.controllers.dtos.base.ResponseDto;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;
import lombok.Getter;

@Getter
public class LoginResponseDto extends ResponseDto {
    private final String accessToken;
    private final String refreshToken;
    private final String tipo;
    private final UsuarioInfoDto usuario;

    public LoginResponseDto(
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

    public static LoginResponseDto fromEntityLogin(UsuarioSistema usuarioSistema, String accessToken, String refreshToken) {
        return new LoginResponseDto(
                accessToken,
                refreshToken,
                "Bearer",
                UsuarioInfoDto.fromEntity(usuarioSistema),
                "auth.login.sucesso"
        );
    }

    public static LoginResponseDto fromEntityRegistro(UsuarioSistema usuarioSistema, String accessToken, String refreshToken) {
        return new LoginResponseDto(
                accessToken,
                refreshToken,
                "Bearer",
                UsuarioInfoDto.fromEntity(usuarioSistema),
                "auth.registro.sucesso"
        );
    }
}

