package io.github.dudupuci.appdespesas.application.usecases.compromisso;

import io.github.dudupuci.appdespesas.application.ports.repositories.CompromissoRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CompromissoService;
import io.github.dudupuci.appdespesas.domain.entities.Compromisso;

import java.util.Date;
import java.util.UUID;

public class ConcluirCompromissoUseCaseImpl extends ConcluirCompromissoUseCase {

    private final CompromissoRepositoryPort compromissoRepository;
    private final CompromissoService compromissoService;
    private final UUID usuarioId;

    public ConcluirCompromissoUseCaseImpl(CompromissoRepositoryPort compromissoRepository,
                                          CompromissoService compromissoService, UUID usuarioId) {
        this.compromissoRepository = compromissoRepository;
        this.compromissoService = compromissoService;
        this.usuarioId = usuarioId;
    }

    @Override
    public void executar(UUID id) {
        Compromisso compromisso = compromissoService.buscarPorId(id);
        if (!compromisso.getUsuarioSistema().getId().equals(usuarioId)) {
            throw new RuntimeException("Sem permissão para concluir este compromisso");
        }
        compromisso.setConcluido(true);
        compromisso.setDataAtualizacao(new Date());
        compromissoRepository.save(compromisso);
    }
}

