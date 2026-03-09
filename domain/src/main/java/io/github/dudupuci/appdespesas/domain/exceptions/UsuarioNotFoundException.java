package io.github.dudupuci.appdespesas.domain.exceptions;

import io.github.dudupuci.appdespesas.domain.exceptions.enums.DomainHttpStatus;

public class UsuarioNotFoundException extends AppDespesasException {

    public UsuarioNotFoundException(String message) {
        super(message, DomainHttpStatus.NOT_FOUND);
    }
}
