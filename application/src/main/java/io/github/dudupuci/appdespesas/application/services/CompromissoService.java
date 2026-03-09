package io.github.dudupuci.appdespesas.application.services;

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
    public Compromisso criar(Compromisso compromisso, UUID usuarioId) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
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
    public Compromisso atualizar(UUID id, Compromisso compromissoAtualizado, UUID usuarioId) {
        Compromisso compromisso = buscarPorId(id);
        if (!compromisso.getUsuarioSistema().getId().equals(usuarioId)) {
            throw new RuntimeException("Sem permissão para editar este compromisso");
        }
        compromisso.setTitulo(compromissoAtualizado.getTitulo());
        compromisso.setDescricao(compromissoAtualizado.getDescricao());
        compromisso.setLocalizacao(compromissoAtualizado.getLocalizacao());
        compromisso.setDataInicio(compromissoAtualizado.getDataInicio());
        compromisso.setDataFim(compromissoAtualizado.getDataFim());
        compromisso.setPrioridade(compromissoAtualizado.getPrioridade());
        compromisso.setDiaInteiro(compromissoAtualizado.getDiaInteiro());
        compromisso.setLembrarEm(compromissoAtualizado.getLembrarEm());
        compromisso.setCor(compromissoAtualizado.getCor());
        compromisso.setObservacoes(compromissoAtualizado.getObservacoes());
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
