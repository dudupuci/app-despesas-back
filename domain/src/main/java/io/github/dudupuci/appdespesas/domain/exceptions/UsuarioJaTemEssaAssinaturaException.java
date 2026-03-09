package io.github.dudupuci.appdespesas.domain.exceptions;

import io.github.dudupuci.appdespesas.domain.exceptions.enums.DomainHttpStatus;

public class UsuarioJaTemEssaAssinaturaException extends AppDespesasException {

    public UsuarioJaTemEssaAssinaturaException(String message) {
        super(message, DomainHttpStatus.CONFLICT);
    }
}
