package io.github.dudupuci.appdespesas.exceptions;

import org.springframework.http.HttpStatus;

public class UsuarioJaTemEssaAssinaturaException extends AppDespesasException {

    public UsuarioJaTemEssaAssinaturaException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}