package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CobrancaRepository extends JpaRepository<Cobranca, UUID> {

    @Query("SELECT c FROM Cobranca c WHERE c.asaasCobrancaId = :asaasCobrancaId")
    Optional<Cobranca> findByAsaasCobrancaId(String asaasCobrancaId);
}
