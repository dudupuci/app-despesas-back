package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.AssinaturaRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaAssinatura;
import io.github.dudupuci.appdespesas.infrastructure.repositories.AssinaturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter para AssinaturaRepository
 */
@Component
@RequiredArgsConstructor
public class AssinaturaRepositoryAdapter implements AssinaturaRepositoryPort {

    private final AssinaturaRepository jpaRepository;

    @Override
    public Assinatura save(Assinatura assinatura) {
        JpaAssinatura jpaAssinatura = JpaAssinatura.fromEntity(assinatura);

        if (assinatura.getId() != null) {
            Optional<JpaAssinatura> existing = jpaRepository.findById(assinatura.getId());
            if (existing.isPresent()) {
                JpaAssinatura existingEntity = existing.get();
                existingEntity.updateFromEntity(assinatura);
                jpaAssinatura = existingEntity;
            }
        }

        JpaAssinatura saved = jpaRepository.save(jpaAssinatura);
        return saved.toEntity();
    }

    @Override
    public Optional<Assinatura> findById(Long id) {
        return jpaRepository.findById(id)
                .map(JpaAssinatura::toEntity);
    }

    @Override
    public List<Assinatura> findAll() {
        return jpaRepository.findAll().stream()
                .map(JpaAssinatura::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Assinatura buscarPorNomePlano(String nomePlano) {
        JpaAssinatura jpaAssinatura = jpaRepository.buscarPorNomePlano(nomePlano);
        return jpaAssinatura != null ? jpaAssinatura.toEntity() : null;
    }

    @Override
    public Assinatura buscarAssinaturaGratuita() {
        JpaAssinatura jpaAssinatura = jpaRepository.buscarAssinaturaGratuita();
        return jpaAssinatura != null ? jpaAssinatura.toEntity() : null;
    }

    @Override
    public Optional<Assinatura> buscarAssinaturaByUsuarioId(UUID usuarioId) {
        return jpaRepository.buscarAssinaturaByUsuarioId(usuarioId)
                .map(JpaAssinatura::toEntity);
    }

    @Override
    public Optional<List<Assinatura>> buscarOutrasAssinaturasByUsuarioId(UUID usuarioId) {
        return jpaRepository.buscarOutrasAssinaturasByUsuarioId(usuarioId)
                .map(list -> list.stream()
                        .map(JpaAssinatura::toEntity)
                        .collect(Collectors.toList()));
    }
}

