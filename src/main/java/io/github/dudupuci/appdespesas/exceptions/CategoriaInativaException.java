package io.github.dudupuci.appdespesas.exceptions;

import org.springframework.http.HttpStatus;

public class CategoriaInativaException extends AppDespesasException {

    public CategoriaInativaException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}