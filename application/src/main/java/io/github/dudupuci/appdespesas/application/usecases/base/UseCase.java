package io.github.dudupuci.appdespesas.application.usecases.base;

/**
 * UseCase com entrada e saída.
 *
 * @param <In>  tipo do comando/input
 * @param <Out> tipo do resultado/output
 */
public abstract class UseCase<In, Out> {
    public abstract Out executar(In input);
}

