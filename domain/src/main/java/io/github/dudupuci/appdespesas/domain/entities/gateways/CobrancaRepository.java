package io.github.dudupuci.appdespesas.application.ports.repositories;

import io.github.dudupuci.appdespesas.domain.entities.Cobranca;

import java.util.Optional;
import java.util.UUID;

/**
 * Port (interface) para o repositório de Cobranças
 */
public interface CobrancaRepositoryPort {

    Cobranca save(Cobranca cobranca);

    Optional<Cobranca> findById(UUID id);

    Optional<Cobranca> findByAsaasCobrancaId(String asaasCobrancaId);

    void delete(Cobranca cobranca);
}

