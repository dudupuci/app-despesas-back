package io.github.dudupuci.appdespesas.exceptions;


import org.springframework.http.HttpStatus;

public class ErroAoCriarCobrancaException extends AppDespesasException {

    public ErroAoCriarCobrancaException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}