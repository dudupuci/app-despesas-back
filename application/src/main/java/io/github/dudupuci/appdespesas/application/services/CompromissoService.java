package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.ports.repositories.CompromissoRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Compromisso;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CompromissoService {

    private final CompromissoRepositoryPort compromissoRepository;
    private final UsuarioService usuarioService;

    public CompromissoService(CompromissoRepositoryPort compromissoRepository, UsuarioService usuarioService) {
        this.compromissoRepository = compromissoRepository;
        this.usuarioService = usuarioService;
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
}
