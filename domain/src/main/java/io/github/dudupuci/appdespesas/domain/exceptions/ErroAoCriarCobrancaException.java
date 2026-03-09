package io.github.dudupuci.appdespesas.domain.exceptions;

import io.github.dudupuci.appdespesas.domain.exceptions.enums.DomainHttpStatus;

public class ErroAoCriarCobrancaException extends AppDespesasException {

    public ErroAoCriarCobrancaException(String message) {
        super(message, DomainHttpStatus.BAD_REQUEST);
    }
}
