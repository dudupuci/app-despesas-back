package io.github.dudupuci.appdespesas.application.usecases.base;

/**
 * UseCase sem entrada, com saída (operações de consulta simples sem parâmetros).
 *
 * @param <Out> tipo do resultado/output
 */
public abstract class NullaryUseCase<Out> {
    public abstract Out executar();
}

