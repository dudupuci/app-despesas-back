package io.github.dudupuci.appdespesas.infrastructure.controllers.noauth.dtos.response.login;

import io.github.dudupuci.appdespesas.application.responses.auth.RefreshTokenResult;
import io.github.dudupuci.appdespesas.infrastructure.controllers.dtos.base.ResponseDto;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasMessages;
import lombok.Getter;

@Getter
public class RefreshTokenResponseDto extends ResponseDto {
    private final String accessToken;

    protected RefreshTokenResponseDto(String mensagemFeedback, String accessToken) {
        super(AppDespesasMessages.getMessage(mensagemFeedback, null));
        this.accessToken = accessToken;
    }

    public static RefreshTokenResponseDto fromResult(RefreshTokenResult result) {
        return new RefreshTokenResponseDto("auth.refreshToken.gerado.sucesso", result.accessToken());
    }

    /** @deprecated use fromResult() */
    public static RefreshTokenResponseDto of(String accessToken) {
        return new RefreshTokenResponseDto("auth.refreshToken.gerado.sucesso", accessToken);
    }
}

