package io.github.dudupuci.appdespesas.domain.exceptions;

import org.springframework.http.HttpStatus;

public class UsuarioJaTemEssaAssinaturaException extends AppDespesasException {

    public UsuarioJaTemEssaAssinaturaException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}