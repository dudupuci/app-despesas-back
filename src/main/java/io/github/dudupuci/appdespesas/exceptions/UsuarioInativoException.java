package io.github.dudupuci.appdespesas.exceptions;

import org.springframework.http.HttpStatus;

public class UsuarioInativoException extends AppDespesasException {
    public UsuarioInativoException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
