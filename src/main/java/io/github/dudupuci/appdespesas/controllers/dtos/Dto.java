package io.github.dudupuci.appdespesas.controllers.dtos;

import io.github.dudupuci.appdespesas.models.entities.base.Entidade;
import lombok.Getter;

@Getter
public abstract class Dto {
    private final String mensagemFeedback;

    protected Dto(String mensagemFeedback) {
        this.mensagemFeedback = mensagemFeedback;
    }

}
