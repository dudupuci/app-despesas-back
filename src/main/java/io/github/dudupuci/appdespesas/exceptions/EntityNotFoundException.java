package io.github.dudupuci.appdespesas.exceptions;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends AppDespesasException {

    public EntityNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}