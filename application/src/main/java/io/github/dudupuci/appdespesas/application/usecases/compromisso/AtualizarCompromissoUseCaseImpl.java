package io.github.dudupuci.appdespesas.application.usecases.compromisso;

import io.github.dudupuci.appdespesas.application.ports.repositories.CompromissoRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CompromissoService;
import io.github.dudupuci.appdespesas.domain.entities.Compromisso;

import java.util.Date;

public class AtualizarCompromissoUseCaseImpl extends AtualizarCompromissoUseCase {

    private final CompromissoRepositoryPort compromissoRepository;
    private final CompromissoService compromissoService;

    public AtualizarCompromissoUseCaseImpl(CompromissoRepositoryPort compromissoRepository,
                                           CompromissoService compromissoService) {
        this.compromissoRepository = compromissoRepository;
        this.compromissoService = compromissoService;
    }

    @Override
    public Compromisso executar(CompromissoCommand cmd) {
        Compromisso compromisso = compromissoService.buscarPorId(cmd.compromissoId());
        if (!compromisso.getUsuarioSistema().getId().equals(cmd.usuarioId()))
            throw new RuntimeException("Sem permissão para editar este compromisso");
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
