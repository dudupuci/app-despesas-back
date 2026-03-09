package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.CobrancaRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaCobranca;
import io.github.dudupuci.appdespesas.infrastructure.repositories.CobrancaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CobrancaRepositoryAdapter implements CobrancaRepositoryPort {

    private final CobrancaRepository jpaRepository;

    @Override
    public Cobranca save(Cobranca cobranca) {
        JpaCobranca jpa = JpaCobranca.fromEntity(cobranca);

        if (cobranca.getId() != null) {
            Optional<JpaCobranca> existing = jpaRepository.findById(cobranca.getId());
            if (existing.isPresent()) {
                JpaCobranca existingEntity = existing.get();
                existingEntity.updateFromEntity(cobranca);
                jpa = existingEntity;
            }
        }

        return jpaRepository.save(jpa).toEntity();
    }

    @Override
    public Optional<Cobranca> findById(UUID id) {
        return jpaRepository.findById(id).map(JpaCobranca::toEntity);
    }

    @Override
    public Optional<Cobranca> findByAsaasCobrancaId(String asaasCobrancaId) {
        return jpaRepository.findByAsaasCobrancaId(asaasCobrancaId).map(JpaCobranca::toEntity);
    }

    @Override
    public void delete(Cobranca cobranca) {
        if (cobranca.getId() != null) {
            jpaRepository.deleteById(cobranca.getId());
        }
    }
}

