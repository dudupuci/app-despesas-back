package io.github.dudupuci.appdespesas.domain.exceptions;


import io.github.dudupuci.appdespesas.domain.exceptions.enums.DomainHttpStatus;

public class CampoObrigatorioException extends AppDespesasException {

    public CampoObrigatorioException(String message) {
        super(message, DomainHttpStatus.BAD_REQUEST);
    }
}
