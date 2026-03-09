package io.github.dudupuci.appdespesas.domain.exceptions;

import org.springframework.http.HttpStatus;

public class FormularioNaoPreenchidoException extends AppDespesasException {

    public FormularioNaoPreenchidoException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
