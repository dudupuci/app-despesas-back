package io.github.dudupuci.appdespesas.exceptions;

import org.springframework.http.HttpStatus;

public abstract class AppDespesasException extends RuntimeException {

    private final HttpStatus httpStatus;

    protected AppDespesasException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}