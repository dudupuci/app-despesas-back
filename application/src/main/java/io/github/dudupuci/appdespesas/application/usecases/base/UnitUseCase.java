package io.github.dudupuci.appdespesas.application.usecases.base;

/**
 * UseCase com entrada, sem saída (operações void como deletar, concluir).
 *
 * @param <In> tipo do comando/input
 */
public abstract class UnitUseCase<In> {
    public abstract void executar(In input);
}

