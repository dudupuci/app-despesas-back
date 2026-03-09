package io.github.dudupuci.appdespesas.domain.exceptions;

import io.github.dudupuci.appdespesas.domain.exceptions.enums.DomainHttpStatus;

public class EntityAlreadyExistsException extends AppDespesasException {

    public EntityAlreadyExistsException(String message) {
        super(message, DomainHttpStatus.CONFLICT);
    }
}
