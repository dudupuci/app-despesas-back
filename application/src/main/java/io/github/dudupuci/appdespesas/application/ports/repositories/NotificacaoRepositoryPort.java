package io.github.dudupuci.appdespesas.application.ports.repositories;

import io.github.dudupuci.appdespesas.domain.entities.base.Notificacao;

import java.util.Optional;
import java.util.UUID;

/**
 * Port (interface) para o repositório de Notificações
 */
public interface NotificacaoRepositoryPort {

    Notificacao save(Notificacao notificacao);

    Optional<Notificacao> findById(UUID id);
}

