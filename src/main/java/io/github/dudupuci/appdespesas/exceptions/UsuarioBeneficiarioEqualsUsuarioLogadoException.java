package io.github.dudupuci.appdespesas.exceptions;


import org.springframework.http.HttpStatus;

public class UsuarioBeneficiarioEqualsUsuarioLogadoException extends AppDespesasException {

    public UsuarioBeneficiarioEqualsUsuarioLogadoException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
