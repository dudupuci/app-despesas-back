package io.github.dudupuci.appdespesas.domain.exceptions;

import org.springframework.http.HttpStatus;

public class SenhasNaoCoincidemException extends AppDespesasException {

    public SenhasNaoCoincidemException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
