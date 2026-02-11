package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.models.entities.Evento;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.repositories.EventoRepository;
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
public class EventoService {

    private static final Logger log = LoggerFactory.getLogger(EventoService.class);

    private final EventoRepository eventoRepository;
    private final UsuariosRepository usuariosRepository;

    public EventoService(
            EventoRepository eventoRepository,
            UsuariosRepository usuariosRepository
    ) {
        this.eventoRepository = eventoRepository;
        this.usuariosRepository = usuariosRepository;
    }

    public Evento criar(Evento evento, UUID usuarioId) {
        Optional<UsuarioSistema> usuario = usuariosRepository.findById(usuarioId);
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        evento.setUsuarioSistema(usuario.get());
        evento.setDataCriacao(new Date());
        evento.setDataAtualizacao(new Date());

        log.info("✅ Evento criado: {} para o usuário: {}",
                evento.getTitulo(), usuario.get().getEmail());

        return eventoRepository.save(evento);
    }

    public Evento buscarPorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento não encontrado"));
    }

    public List<Evento> listarTodos(UUID usuarioId) {
        Optional<UsuarioSistema> usuario = usuariosRepository.findById(usuarioId);
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        return eventoRepository.findByUsuarioSistema(usuario.get());
    }

    public List<Evento> listarPorPeriodo(UUID usuarioId, Date dataInicio, Date dataFim) {
        Optional<UsuarioSistema> usuario = usuariosRepository.findById(usuarioId);
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        return eventoRepository.findByUsuarioAndPeriodo(usuario.get(), dataInicio, dataFim);
    }

    public List<Evento> listarRecorrentes(UUID usuarioId) {
        Optional<UsuarioSistema> usuario = usuariosRepository.findById(usuarioId);
        if (usuario.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado");
        }
        return eventoRepository.findEventosRecorrentesByUsuario(usuario.get());
    }

    public Evento atualizar(Long id, Evento eventoAtualizado) {
        Evento evento = buscarPorId(id);

        evento.setTitulo(eventoAtualizado.getTitulo());
        evento.setDescricao(eventoAtualizado.getDescricao());
        evento.setDataInicio(eventoAtualizado.getDataInicio());
        evento.setDataFim(eventoAtualizado.getDataFim());
        evento.setIsRecorrente(eventoAtualizado.getIsRecorrente());
        evento.setFrequenciaRecorrencia(eventoAtualizado.getFrequenciaRecorrencia());
        evento.setDataFimRecorrencia(eventoAtualizado.getDataFimRecorrencia());
        evento.setPrioridade(eventoAtualizado.getPrioridade());
        evento.setCor(eventoAtualizado.getCor());
        evento.setDiaInteiro(eventoAtualizado.getDiaInteiro());
        evento.setObservacoes(eventoAtualizado.getObservacoes());
        evento.setDataAtualizacao(new Date());

        log.info("✏️ Evento atualizado: ID {}", id);

        return eventoRepository.save(evento);
    }

    public void deletar(Long id) {
        Evento evento = buscarPorId(id);
        log.info("🗑️ Evento deletado: {}", evento.getTitulo());
        eventoRepository.delete(evento);
    }
}

