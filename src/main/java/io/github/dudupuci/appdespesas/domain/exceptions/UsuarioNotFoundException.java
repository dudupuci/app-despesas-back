package io.github.dudupuci.appdespesas.domain.exceptions;

import org.springframework.http.HttpStatus;

public class UsuarioNotFoundException extends AppDespesasException {

    public UsuarioNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
