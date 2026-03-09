package io.github.dudupuci.appdespesas.domain.exceptions;

import org.springframework.http.HttpStatus;

public class ErroAoObterQrCodePixException extends AppDespesasException {
    public ErroAoObterQrCodePixException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
