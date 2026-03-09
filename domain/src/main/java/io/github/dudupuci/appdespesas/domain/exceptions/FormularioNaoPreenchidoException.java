package io.github.dudupuci.appdespesas.domain.exceptions;

import io.github.dudupuci.appdespesas.domain.exceptions.enums.DomainHttpStatus;

public class FormularioNaoPreenchidoException extends AppDespesasException {

    public FormularioNaoPreenchidoException(String message) {
        super(message, DomainHttpStatus.BAD_REQUEST);
    }
}
