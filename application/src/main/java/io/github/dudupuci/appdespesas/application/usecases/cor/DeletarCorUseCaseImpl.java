package io.github.dudupuci.appdespesas.application.usecases.cor;

import io.github.dudupuci.appdespesas.application.ports.repositories.CorRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CorService;
import io.github.dudupuci.appdespesas.domain.entities.Cor;

import java.util.UUID;

public class DeletarCorUseCaseImpl extends DeletarCorUseCase {

    private final CorRepositoryPort corRepository;
    private final CorService corService;
    private final UUID usuarioId;

    public DeletarCorUseCaseImpl(CorRepositoryPort corRepository, CorService corService, UUID usuarioId) {
        this.corRepository = corRepository;
        this.corService = corService;
        this.usuarioId = usuarioId;
    }

    @Override
    public void executar(UUID id) {
        Cor cor = corService.buscarPorId(id, usuarioId);
        corRepository.delete(cor);
    }
}

