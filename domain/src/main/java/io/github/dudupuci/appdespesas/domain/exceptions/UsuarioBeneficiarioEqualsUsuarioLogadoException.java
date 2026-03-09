package io.github.dudupuci.appdespesas.domain.exceptions;


import io.github.dudupuci.appdespesas.domain.exceptions.enums.DomainHttpStatus;

public class UsuarioBeneficiarioEqualsUsuarioLogadoException extends AppDespesasException {

    public UsuarioBeneficiarioEqualsUsuarioLogadoException(String message) {
        super(message, DomainHttpStatus.CONFLICT);
    }
}
