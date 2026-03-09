package io.github.dudupuci.appdespesas.domain.exceptions;


import io.github.dudupuci.appdespesas.domain.exceptions.enums.DomainHttpStatus;

public class UsuarioInativoException extends AppDespesasException {
    public UsuarioInativoException(String message) {
        super(message, DomainHttpStatus.BAD_REQUEST);
    }
}
