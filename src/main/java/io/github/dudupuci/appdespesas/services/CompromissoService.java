package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.models.entities.Compromisso;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.repositories.CompromissoRepository;
import io.github.dudupuci.appdespesas.repositories.UsuariosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CompromissoService {

    private static final Logger log = LoggerFactory.getLogger(CompromissoService.class);

    private final CompromissoRepository compromissoRepository;
    private final UsuariosRepository usuariosRepository;

    public CompromissoService(
            CompromissoRepository compromissoRepository,
            UsuariosRepository usuariosRepository
    ) {
        this.compromissoRepository = compromissoRepository;
        this.usuariosRepository = usuariosRepository;
    }

    @Transactional
    public Compromisso criar(Compromisso compromisso, UUID usuarioId) {
         UsuarioSistema usuario = usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        compromisso.setUsuarioSistema(usuario);
        compromisso.setDataCriacao(new Date());
        compromisso.setDataAtualizacao(new Date());

        log.info("✅ Compromisso criado: {} para o usuário: {}",
                compromisso.getTitulo(), usuario.getEmail());

        return compromissoRepository.save(compromisso);
    }

    public Compromisso buscarPorId(Long id) {
        return compromissoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Compromisso não encontrado"));
    }

    public List<Compromisso> listarTodos(UUID usuarioId) {
        Optional<UsuarioSistema> usuario = usuariosRepository.findById(usuarioId);
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        return compromissoRepository.findByUsuarioSistema(usuario.get());
    }

    public List<Compromisso> listarPorPeriodo(UUID usuarioId, Date dataInicio, Date dataFim) {
        Optional<UsuarioSistema> usuario = usuariosRepository.findById((usuarioId));
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        return compromissoRepository.findByUsuarioAndPeriodo(usuario.get(), dataInicio, dataFim);
    }

    public List<Compromisso> listarPendentes(UUID usuarioId) {
        Optional<UsuarioSistema> usuario = usuariosRepository.findById((usuarioId));
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        return compromissoRepository.findByUsuarioAndConcluido(usuario.get(), false);
    }

    public List<Compromisso> listarConcluidos(UUID usuarioId) {
        Optional<UsuarioSistema> usuario = usuariosRepository.findById((usuarioId));
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        return compromissoRepository.findByUsuarioAndConcluido(usuario.get(), true);
    }

    public Compromisso atualizar(Long id, Compromisso compromissoAtualizado) {
        Compromisso compromisso = buscarPorId(id);

        compromisso.setTitulo(compromissoAtualizado.getTitulo());
        compromisso.setDescricao(compromissoAtualizado.getDescricao());
        compromisso.setDataInicio(compromissoAtualizado.getDataInicio());
        compromisso.setDataFim(compromissoAtualizado.getDataFim());
        compromisso.setDiaInteiro(compromissoAtualizado.getDiaInteiro());
        compromisso.setPrioridade(compromissoAtualizado.getPrioridade());
        compromisso.setLocalizacao(compromissoAtualizado.getLocalizacao());
        compromisso.setCor(compromissoAtualizado.getCor());
        compromisso.setLembrarEm(compromissoAtualizado.getLembrarEm());
        compromisso.setObservacoes(compromissoAtualizado.getObservacoes());
        compromisso.setDataAtualizacao(new Date());

        log.info("✏️ Compromisso atualizado: ID {}", id);

        return compromissoRepository.save(compromisso);
    }

    public Compromisso marcarComoConcluido(Long id) {
        Compromisso compromisso = buscarPorId(id);
        compromisso.setConcluido(true);
        compromisso.setDataConclusao(new Date());
        compromisso.setDataAtualizacao(new Date());

        log.info("✅ Compromisso concluído: {}", compromisso.getTitulo());

        return compromissoRepository.save(compromisso);
    }

    public Compromisso desmarcarConclusao(Long id) {
        Compromisso compromisso = buscarPorId(id);
        compromisso.setConcluido(false);
        compromisso.setDataConclusao(null);
        compromisso.setDataAtualizacao(new Date());

        log.info("↩️ Compromisso desmarcado: {}", compromisso.getTitulo());

        return compromissoRepository.save(compromisso);
    }

    public void deletar(Long id) {
        Compromisso compromisso = buscarPorId(id);
        log.info("🗑️ Compromisso deletado: {}", compromisso.getTitulo());
        compromissoRepository.delete(compromisso);
    }
}

