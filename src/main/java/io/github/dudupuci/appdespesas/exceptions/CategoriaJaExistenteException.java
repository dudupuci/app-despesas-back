package io.github.dudupuci.appdespesas.exceptions;

public class CategoriaJaExistenteException extends NotFoundException {

    public CategoriaJaExistenteException(String message) {
        super(message);
    }
}
