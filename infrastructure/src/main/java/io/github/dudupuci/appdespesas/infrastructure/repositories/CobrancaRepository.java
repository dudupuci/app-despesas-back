package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaCobranca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CobrancaRepository extends JpaRepository<JpaCobranca, UUID> {

    @Query("SELECT c FROM JpaCobranca c WHERE c.asaasCobrancaId = :asaasCobrancaId")
    Optional<JpaCobranca> findByAsaasCobrancaId(String asaasCobrancaId);
}
