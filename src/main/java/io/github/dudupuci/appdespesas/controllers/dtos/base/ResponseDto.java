package io.github.dudupuci.appdespesas.controllers.dtos.base;

import lombok.Getter;

@Getter
public abstract class ResponseDto extends Dto {
    private final String mensagemFeedback;

    protected ResponseDto(String mensagemFeedback) {
        this.mensagemFeedback = mensagemFeedback;
    }

}
