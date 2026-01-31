package io.github.dudupuci.appdespesas.exceptions;

public class CategoriaJaExisteException extends EntityNotFoundException {

    public CategoriaJaExisteException(String message) {
        super(message);
    }
}
