package io.github.dudupuci.appdespesas.domain.exceptions;


import io.github.dudupuci.appdespesas.domain.exceptions.enums.DomainHttpStatus;

public class ErroAoObterQrCodePixException extends AppDespesasException {
    public ErroAoObterQrCodePixException(String message) {
        super(message, DomainHttpStatus.BAD_REQUEST);
    }
}
