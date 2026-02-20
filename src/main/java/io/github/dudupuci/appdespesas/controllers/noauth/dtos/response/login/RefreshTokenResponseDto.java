package io.github.dudupuci.appdespesas.controllers.noauth.dtos.response.login;

import io.github.dudupuci.appdespesas.controllers.dtos.base.ResponseDto;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;
import lombok.Getter;

@Getter
public class RefreshTokenResponseDto extends ResponseDto {
    private final String accessToken;

    protected RefreshTokenResponseDto(String mensagemFeedback, String accessToken) {
        super(AppDespesasMessages.getMessage(mensagemFeedback, null));
        this.accessToken = accessToken;
    }

    public static RefreshTokenResponseDto of(String accessToken) {
        return new RefreshTokenResponseDto("auth.refreshToken.gerado.sucesso", accessToken);
    }
}

