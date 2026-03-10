package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.AssinaturaRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.AssinaturaJpaEntity;
import io.github.dudupuci.appdespesas.infrastructure.repositories.AssinaturaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Adapter para AssinaturaJpaRepository
 */
@Component
@RequiredArgsConstructor
public class AssinaturaRepositoryAdapter implements AssinaturaRepositoryPort {

    private final AssinaturaJpaRepository jpaRepository;

    @Override
    public Assinatura save(Assinatura assinatura) {
        AssinaturaJpaEntity assinaturaJpaEntity = AssinaturaJpaEntity.fromEntity(assinatura);

        if (assinatura.getId() != null) {
            Optional<AssinaturaJpaEntity> existing = jpaRepository.findById(assinatura.getId());
            if (existing.isPresent()) {
                AssinaturaJpaEntity existingEntity = existing.get();
                existingEntity.updateFromEntity(assinatura);
                assinaturaJpaEntity = existingEntity;
            }
        }

        AssinaturaJpaEntity saved = jpaRepository.save(assinaturaJpaEntity);
        return saved.toEntity();
    }

    @Override
    public Optional<Assinatura> findById(Long id) {
        return jpaRepository.findById(id)
                .map(AssinaturaJpaEntity::toEntity);
    }

    @Override
    public List<Assinatura> findAll() {
        return jpaRepository.findAll().stream()
                .map(AssinaturaJpaEntity::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Assinatura buscarPorNomePlano(String nomePlano) {
        AssinaturaJpaEntity assinaturaJpaEntity = jpaRepository.buscarPorNomePlano(nomePlano);
        return assinaturaJpaEntity != null ? assinaturaJpaEntity.toEntity() : null;
    }

    @Override
    public Assinatura buscarAssinaturaGratuita() {
        AssinaturaJpaEntity assinaturaJpaEntity = jpaRepository.buscarAssinaturaGratuita();
        return assinaturaJpaEntity != null ? assinaturaJpaEntity.toEntity() : null;
    }

    @Override
    public Optional<Assinatura> buscarAssinaturaByUsuarioId(UUID usuarioId) {
        return jpaRepository.buscarAssinaturaByUsuarioId(usuarioId)
                .map(AssinaturaJpaEntity::toEntity);
    }

    @Override
    public Optional<List<Assinatura>> buscarOutrasAssinaturasByUsuarioId(UUID usuarioId) {
        return jpaRepository.buscarOutrasAssinaturasByUsuarioId(usuarioId)
                .map(list -> list.stream()
                        .map(AssinaturaJpaEntity::toEntity)
                        .collect(Collectors.toList()));
    }
}

