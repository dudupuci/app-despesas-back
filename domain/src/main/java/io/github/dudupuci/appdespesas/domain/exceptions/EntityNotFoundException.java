package io.github.dudupuci.appdespesas.domain.exceptions;

import io.github.dudupuci.appdespesas.domain.exceptions.enums.DomainHttpStatus;

public class EntityNotFoundException extends AppDespesasException {

    public EntityNotFoundException(String message) {
        super(message, DomainHttpStatus.NOT_FOUND);
    }
}
