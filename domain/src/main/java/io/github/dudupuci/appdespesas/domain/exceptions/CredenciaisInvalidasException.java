package io.github.dudupuci.appdespesas.domain.exceptions;


import io.github.dudupuci.appdespesas.domain.exceptions.enums.DomainHttpStatus;

public class CredenciaisInvalidasException extends AppDespesasException {
    public CredenciaisInvalidasException(String message) {
        super(message, DomainHttpStatus.BAD_REQUEST);
    }
}

