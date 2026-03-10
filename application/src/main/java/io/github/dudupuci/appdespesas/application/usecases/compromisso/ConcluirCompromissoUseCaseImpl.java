package io.github.dudupuci.appdespesas.application.usecases.compromisso;

import io.github.dudupuci.appdespesas.application.ports.repositories.CompromissoRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CompromissoService;
import io.github.dudupuci.appdespesas.application.usecases.base.DeleteCommand;
import io.github.dudupuci.appdespesas.domain.entities.Compromisso;

import java.util.Date;

public class ConcluirCompromissoUseCaseImpl extends ConcluirCompromissoUseCase {

    private final CompromissoRepositoryPort compromissoRepository;
    private final CompromissoService compromissoService;

    public ConcluirCompromissoUseCaseImpl(CompromissoRepositoryPort compromissoRepository,
                                          CompromissoService compromissoService) {
        this.compromissoRepository = compromissoRepository;
        this.compromissoService = compromissoService;
    }

    @Override
    public void executar(DeleteCommand cmd) {
        Compromisso compromisso = compromissoService.buscarPorId(cmd.id());
        if (!compromisso.getUsuarioSistema().getId().equals(cmd.usuarioId()))
            throw new RuntimeException("Sem permissão para concluir este compromisso");
        compromisso.setConcluido(true);
        compromisso.setDataAtualizacao(new Date());
        compromissoRepository.save(compromisso);
    }
}
