package io.github.dudupuci.appdespesas.exceptions;

import org.springframework.http.HttpStatus;

public class UsuarioNotFoundException extends AppDespesasException {

    public UsuarioNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
