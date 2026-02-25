package io.github.dudupuci.appdespesas.exceptions;

import org.springframework.http.HttpStatus;

public class ErroAoObterQrCodePixException extends AppDespesasException {
    public ErroAoObterQrCodePixException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
