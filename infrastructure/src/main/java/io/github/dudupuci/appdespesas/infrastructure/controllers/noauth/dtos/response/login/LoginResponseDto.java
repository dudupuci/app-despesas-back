package io.github.dudupuci.appdespesas.infrastructure.controllers.noauth.dtos.response.login;

import io.github.dudupuci.appdespesas.application.responses.auth.AuthResult;
import io.github.dudupuci.appdespesas.infrastructure.controllers.dtos.base.ResponseDto;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasMessages;
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

    public static LoginResponseDto fromResultLogin(AuthResult result) {
        return new LoginResponseDto(
                result.accessToken(),
                result.refreshToken(),
                "Bearer",
                UsuarioInfoDto.fromEntity(result.usuario()),
                "auth.login.sucesso"
        );
    }

    public static LoginResponseDto fromResultRegistro(AuthResult result) {
        return new LoginResponseDto(
                result.accessToken(),
                result.refreshToken(),
                "Bearer",
                UsuarioInfoDto.fromEntity(result.usuario()),
                "auth.registro.sucesso"
        );
    }

    /** @deprecated use fromResultLogin() ou fromResultRegistro() */
    public static LoginResponseDto fromEntityLogin(UsuarioSistema usuarioSistema, String accessToken, String refreshToken) {
        return new LoginResponseDto(accessToken, refreshToken, "Bearer",
                UsuarioInfoDto.fromEntity(usuarioSistema), "auth.login.sucesso");
    }

    /** @deprecated use fromResultLogin() ou fromResultRegistro() */
    public static LoginResponseDto fromEntityRegistro(UsuarioSistema usuarioSistema, String accessToken, String refreshToken) {
        return new LoginResponseDto(accessToken, refreshToken, "Bearer",
                UsuarioInfoDto.fromEntity(usuarioSistema), "auth.registro.sucesso");
    }
}

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

