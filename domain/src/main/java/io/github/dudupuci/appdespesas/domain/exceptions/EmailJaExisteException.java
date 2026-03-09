package io.github.dudupuci.appdespesas.domain.exceptions;

import io.github.dudupuci.appdespesas.domain.exceptions.enums.DomainHttpStatus;

public class EmailJaExisteException extends AppDespesasException {
    public EmailJaExisteException(String message) {
        super(message, DomainHttpStatus.CONFLICT);
    }
}
