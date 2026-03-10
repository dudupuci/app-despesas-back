package io.github.dudupuci.appdespesas.application.usecases.compromisso;

import io.github.dudupuci.appdespesas.application.ports.repositories.CompromissoRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CompromissoService;
import io.github.dudupuci.appdespesas.domain.entities.Compromisso;

import java.util.Date;
import java.util.UUID;

public class AtualizarCompromissoUseCaseImpl extends AtualizarCompromissoUseCase {

    private final CompromissoRepositoryPort compromissoRepository;
    private final CompromissoService compromissoService;
    private final UUID id;
    private final UUID usuarioId;

    public AtualizarCompromissoUseCaseImpl(CompromissoRepositoryPort compromissoRepository,
                                           CompromissoService compromissoService, UUID id, UUID usuarioId) {
        this.compromissoRepository = compromissoRepository;
        this.compromissoService = compromissoService;
        this.id = id;
        this.usuarioId = usuarioId;
    }

    @Override
    public Compromisso executar(CompromissoCommand cmd) {
        Compromisso compromisso = compromissoService.buscarPorId(id);
        if (!compromisso.getUsuarioSistema().getId().equals(usuarioId)) {
            throw new RuntimeException("Sem permissão para editar este compromisso");
        }
        compromisso.setTitulo(cmd.titulo());
        compromisso.setDescricao(cmd.descricao());
        compromisso.setLocalizacao(cmd.localizacao());
        compromisso.setDataInicio(cmd.dataInicio());
        compromisso.setDataFim(cmd.dataFim());
        compromisso.setPrioridade(cmd.prioridade());
        compromisso.setDiaInteiro(cmd.diaInteiro());
        compromisso.setCor(cmd.cor());
        compromisso.setObservacoes(cmd.observacoes());
        compromisso.setDataAtualizacao(new Date());
        return compromissoRepository.save(compromisso);
    }
}

