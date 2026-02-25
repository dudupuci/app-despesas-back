package io.github.dudupuci.appdespesas.exceptions;

import org.springframework.http.HttpStatus;

public class CredenciaisInvalidasException extends AppDespesasException {
    public CredenciaisInvalidasException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

