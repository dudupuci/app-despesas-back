package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.CobrancaRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.CobrancaJpaEntity;
import io.github.dudupuci.appdespesas.infrastructure.repositories.CobrancaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CobrancaRepositoryAdapter implements CobrancaRepositoryPort {

    private final CobrancaJpaRepository jpaRepository;

    @Override
    public Cobranca save(Cobranca cobranca) {
        CobrancaJpaEntity jpa = CobrancaJpaEntity.fromEntity(cobranca);

        if (cobranca.getId() != null) {
            Optional<CobrancaJpaEntity> existing = jpaRepository.findById(cobranca.getId());
            if (existing.isPresent()) {
                CobrancaJpaEntity existingEntity = existing.get();
                existingEntity.updateFromEntity(cobranca);
                jpa = existingEntity;
            }
        }

        return jpaRepository.save(jpa).toEntity();
    }

    @Override
    public Optional<Cobranca> findById(UUID id) {
        return jpaRepository.findById(id).map(CobrancaJpaEntity::toEntity);
    }

    @Override
    public Optional<Cobranca> findByAsaasCobrancaId(String asaasCobrancaId) {
        return jpaRepository.findByAsaasCobrancaId(asaasCobrancaId).map(CobrancaJpaEntity::toEntity);
    }

    @Override
    public void delete(Cobranca cobranca) {
        if (cobranca.getId() != null) {
            jpaRepository.deleteById(cobranca.getId());
        }
    }
}

