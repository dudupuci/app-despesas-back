package io.github.dudupuci.appdespesas.application.usecases.compromisso;

import io.github.dudupuci.appdespesas.application.ports.repositories.CompromissoRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.domain.entities.Compromisso;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;

import java.util.Date;

public class CriarCompromissoUseCaseImpl extends CriarCompromissoUseCase {

    private final CompromissoRepositoryPort compromissoRepository;
    private final UsuarioService usuarioService;

    public CriarCompromissoUseCaseImpl(CompromissoRepositoryPort compromissoRepository, UsuarioService usuarioService) {
        this.compromissoRepository = compromissoRepository;
        this.usuarioService = usuarioService;
    }

    @Override
    public Compromisso executar(CompromissoCommand cmd) {
        UsuarioSistema usuario = usuarioService.buscarPorId(cmd.usuarioId());
        Compromisso compromisso = new Compromisso();
        compromisso.setTitulo(cmd.titulo());
        compromisso.setDescricao(cmd.descricao());
        compromisso.setDataInicio(cmd.dataInicio());
        compromisso.setDataFim(cmd.dataFim());
        compromisso.setDiaInteiro(cmd.diaInteiro());
        compromisso.setPrioridade(cmd.prioridade());
        compromisso.setLocalizacao(cmd.localizacao());
        compromisso.setCor(cmd.cor());
        compromisso.setObservacoes(cmd.observacoes());
        compromisso.setUsuarioSistema(usuario);
        compromisso.setDataCriacao(new Date());
        compromisso.setDataAtualizacao(new Date());
        return compromissoRepository.save(compromisso);
    }
}
