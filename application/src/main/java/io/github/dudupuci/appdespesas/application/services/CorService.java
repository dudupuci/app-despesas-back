package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.ports.repositories.CorRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Cor;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

public class CorService {

    private final CorRepositoryPort corRepository;
    private final UsuarioService usuarioService;

    public CorService(CorRepositoryPort corRepository, UsuarioService usuarioService) {
        this.corRepository = corRepository;
        this.usuarioService = usuarioService;
    }

    public Cor buscarPorId(UUID id, UUID usuarioId) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        return corRepository.findByIdAndUsuarioSistema(id, usuario)
                .orElseThrow(() -> new EntityNotFoundException("Cor não encontrada"));
    }

    public List<Cor> listarTodas(UUID usuarioId) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        return corRepository.listarTodasPorUsuarioId(usuario.getId());
    }
}
