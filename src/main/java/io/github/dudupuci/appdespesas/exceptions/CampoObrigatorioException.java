package io.github.dudupuci.appdespesas.exceptions;


import org.springframework.http.HttpStatus;

public class CampoObrigatorioException extends AppDespesasException {

    public CampoObrigatorioException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}