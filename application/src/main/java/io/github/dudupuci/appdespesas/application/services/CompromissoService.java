package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.commands.compromisso.CompromissoCommand;
import io.github.dudupuci.appdespesas.application.ports.repositories.CompromissoRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Compromisso;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class CompromissoService {

    private static final Logger log = LoggerFactory.getLogger(CompromissoService.class);

    private final CompromissoRepositoryPort compromissoRepository;
    private final UsuarioService usuarioService;

    public CompromissoService(CompromissoRepositoryPort compromissoRepository, UsuarioService usuarioService) {
        this.compromissoRepository = compromissoRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public Compromisso criar(CompromissoCommand cmd, UUID usuarioId) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
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
        log.info("✅ Compromisso criado: {} para o usuário: {}", compromisso.getTitulo(), usuario.getContato().getEmail());
        return compromissoRepository.save(compromisso);
    }

    public Compromisso buscarPorId(UUID id) {
        return compromissoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compromisso não encontrado"));
    }

    public List<Compromisso> listarTodos(UUID usuarioId) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        return compromissoRepository.findByUsuarioSistema(usuario);
    }

    public List<Compromisso> listarPorPeriodo(UUID usuarioId, Date dataInicio, Date dataFim) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        return compromissoRepository.findByUsuarioAndPeriodo(usuario, dataInicio, dataFim);
    }

    public List<Compromisso> listarPendentes(UUID usuarioId) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        return compromissoRepository.findByUsuarioAndConcluido(usuario, false);
    }

    public List<Compromisso> listarConcluidos(UUID usuarioId) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        return compromissoRepository.findByUsuarioAndConcluido(usuario, true);
    }

    @Transactional
    public Compromisso atualizar(UUID id, CompromissoCommand cmd, UUID usuarioId) {
        Compromisso compromisso = buscarPorId(id);
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

    @Transactional
    public void concluir(UUID id, UUID usuarioId) {
        Compromisso compromisso = buscarPorId(id);
        if (!compromisso.getUsuarioSistema().getId().equals(usuarioId)) {
            throw new RuntimeException("Sem permissão para concluir este compromisso");
        }
        compromisso.setConcluido(true);
        compromisso.setDataConclusao(new Date());
        compromisso.setDataAtualizacao(new Date());
        compromissoRepository.save(compromisso);
    }

    @Transactional
    public void deletar(UUID id, UUID usuarioId) {
        Compromisso compromisso = buscarPorId(id);
        if (!compromisso.getUsuarioSistema().getId().equals(usuarioId)) {
            throw new RuntimeException("Sem permissão para deletar este compromisso");
        }
        compromissoRepository.delete(compromisso);
    }
}
