package io.github.dudupuci.appdespesas.domain.exceptions;

import io.github.dudupuci.appdespesas.domain.exceptions.enums.DomainHttpStatus;

public class SenhasNaoCoincidemException extends AppDespesasException {

    public SenhasNaoCoincidemException(String message) {
        super(message, DomainHttpStatus.BAD_REQUEST);
    }
}
