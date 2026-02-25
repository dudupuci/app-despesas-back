package io.github.dudupuci.appdespesas.exceptions;

import org.springframework.http.HttpStatus;

public class EntityAlreadyExistsException extends AppDespesasException {

    public EntityAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}