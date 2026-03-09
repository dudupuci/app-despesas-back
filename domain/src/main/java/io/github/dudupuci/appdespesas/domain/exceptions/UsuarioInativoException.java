package io.github.dudupuci.appdespesas.domain.exceptions;

import org.springframework.http.HttpStatus;

public class UsuarioInativoException extends AppDespesasException {
    public UsuarioInativoException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
