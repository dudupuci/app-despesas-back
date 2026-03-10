package io.github.dudupuci.appdespesas.application.usecases.cor;

import io.github.dudupuci.appdespesas.application.ports.repositories.CorRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CorService;
import io.github.dudupuci.appdespesas.application.usecases.base.DeleteCommand;
import io.github.dudupuci.appdespesas.domain.entities.Cor;

public class DeletarCorUseCaseImpl extends DeletarCorUseCase {

    private final CorRepositoryPort corRepository;
    private final CorService corService;

    public DeletarCorUseCaseImpl(CorRepositoryPort corRepository, CorService corService) {
        this.corRepository = corRepository;
        this.corService = corService;
    }

    @Override
    public void executar(DeleteCommand cmd) {
        Cor cor = corService.buscarPorId(cmd.id(), cmd.usuarioId());
        corRepository.delete(cor);
    }
}
