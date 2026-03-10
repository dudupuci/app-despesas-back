package io.github.dudupuci.appdespesas.application.ports.repositories;

import io.github.dudupuci.appdespesas.domain.entities.Assinatura;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port (interface) para o repositório de Assinaturas
 */
public interface AssinaturaRepositoryPort {

    Assinatura save(Assinatura assinatura);

    Optional<Assinatura> findById(Long id);

    List<Assinatura> findAll();

    Assinatura buscarPorNomePlano(String nomePlano);

    Assinatura buscarAssinaturaGratuita();

    Optional<Assinatura> buscarAssinaturaByUsuarioId(UUID usuarioId);

    Optional<List<Assinatura>> buscarOutrasAssinaturasByUsuarioId(UUID usuarioId);
}

