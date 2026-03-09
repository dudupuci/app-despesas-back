package io.github.dudupuci.appdespesas.domain.exceptions;

import io.github.dudupuci.appdespesas.domain.exceptions.enums.DomainHttpStatus;

public abstract class AppDespesasException extends RuntimeException {

    private final DomainHttpStatus httpStatus;

    protected AppDespesasException(String message, DomainHttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public DomainHttpStatus getStatus() {
        return httpStatus;
    }

    public int getStatusCode() {
        return httpStatus.getValue();
    }
}
