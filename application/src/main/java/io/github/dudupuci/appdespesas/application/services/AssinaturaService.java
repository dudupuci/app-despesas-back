package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.ports.repositories.AssinaturaRepositoryPort;
import io.github.dudupuci.appdespesas.domain.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.domain.exceptions.UsuarioJaTemEssaAssinaturaException;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AssinaturaService {

    private final AssinaturaRepositoryPort assinaturaRepository;

    public AssinaturaService(AssinaturaRepositoryPort assinaturaRepository) {
        this.assinaturaRepository = assinaturaRepository;
    }

    public List<Assinatura> listarAssinaturas() {
        return this.assinaturaRepository.findAll();
    }

    public Assinatura buscarAssinaturaPorId(Long id) {
        return this.assinaturaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assinatura não encontrada"));
    }

    public Assinatura buscarAssinaturaByUsuarioId(UUID usuarioId) {
        return this.assinaturaRepository.buscarAssinaturaByUsuarioId(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Assinatura do usuário não encontrada"));
    }

    public List<Assinatura> buscarOutrasAssinaturasByUsuarioId(UUID usuarioId) {
        return this.assinaturaRepository.buscarOutrasAssinaturasByUsuarioId(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Assinaturas do usuário não encontradas"));
    }

    public void validarAssinatura(Assinatura assinatura, UsuarioSistema usuario) {
        if (usuario.getAssinatura() != null && usuario.getAssinatura().getId().equals(assinatura.getId())) {
            throw new UsuarioJaTemEssaAssinaturaException("Você já possui esta assinatura.");
        }
    }

}
