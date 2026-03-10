package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.CobrancaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CobrancaJpaRepository extends JpaRepository<CobrancaJpaEntity, UUID> {

    @Query("SELECT c FROM CobrancaJpaEntity c WHERE c.asaasCobrancaId = :asaasCobrancaId")
    Optional<CobrancaJpaEntity> findByAsaasCobrancaId(String asaasCobrancaId);
}
