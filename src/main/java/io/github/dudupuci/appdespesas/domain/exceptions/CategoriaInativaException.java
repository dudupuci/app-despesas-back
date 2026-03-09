package io.github.dudupuci.appdespesas.domain.exceptions;

import org.springframework.http.HttpStatus;

public class CategoriaInativaException extends AppDespesasException {

    public CategoriaInativaException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}