package io.github.dudupuci.appdespesas.domain.exceptions;

import org.springframework.http.HttpStatus;

public class EmailJaExisteException extends AppDespesasException {
    public EmailJaExisteException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
