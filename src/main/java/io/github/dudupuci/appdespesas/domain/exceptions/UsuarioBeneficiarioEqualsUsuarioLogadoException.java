package io.github.dudupuci.appdespesas.domain.exceptions;


import org.springframework.http.HttpStatus;

public class UsuarioBeneficiarioEqualsUsuarioLogadoException extends AppDespesasException {

    public UsuarioBeneficiarioEqualsUsuarioLogadoException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
