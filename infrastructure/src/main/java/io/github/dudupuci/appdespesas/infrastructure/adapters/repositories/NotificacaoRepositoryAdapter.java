package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.NotificacaoRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.NotificacaoEmail;
import io.github.dudupuci.appdespesas.domain.entities.base.Notificacao;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaNotificacaoEmail;
import io.github.dudupuci.appdespesas.infrastructure.repositories.NotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificacaoRepositoryAdapter implements NotificacaoRepositoryPort {

    private final NotificacaoRepository jpaRepository;

    @Override
    public Notificacao save(Notificacao notificacao) {
        if (notificacao instanceof NotificacaoEmail notificacaoEmail) {
            JpaNotificacaoEmail jpa = JpaNotificacaoEmail.fromEntity(notificacaoEmail);
            return jpaRepository.save(jpa).toEntity();
        }
        throw new IllegalArgumentException("Tipo de notificação não suportado: " + notificacao.getClass().getSimpleName());
    }

    @Override
    public Optional<Notificacao> findById(UUID id) {
        return jpaRepository.findById(id).map(JpaNotificacaoEmail::toEntity);
    }
}

