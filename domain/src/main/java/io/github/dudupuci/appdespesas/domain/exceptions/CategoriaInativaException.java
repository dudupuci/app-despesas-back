package io.github.dudupuci.appdespesas.domain.exceptions;


import io.github.dudupuci.appdespesas.domain.exceptions.enums.DomainHttpStatus;

public class CategoriaInativaException extends AppDespesasException {

    public CategoriaInativaException(String message) {
        super(message, DomainHttpStatus.BAD_REQUEST);
    }
}
