package io.github.dudupuci.appdespesas.exceptions;

import org.springframework.http.HttpStatus;

public class FormularioNaoPreenchidoException extends AppDespesasException {

    public FormularioNaoPreenchidoException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
