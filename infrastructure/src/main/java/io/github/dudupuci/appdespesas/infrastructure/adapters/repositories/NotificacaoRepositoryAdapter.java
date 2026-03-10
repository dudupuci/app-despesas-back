package io.github.dudupuci.appdespesas.infrastructure.adapters.repositories;

import io.github.dudupuci.appdespesas.application.ports.repositories.NotificacaoRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.NotificacaoEmail;
import io.github.dudupuci.appdespesas.domain.entities.base.Notificacao;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.NotificacaoEmailJpaEntity;
import io.github.dudupuci.appdespesas.infrastructure.repositories.NotificacaoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotificacaoRepositoryAdapter implements NotificacaoRepositoryPort {

    private final NotificacaoJpaRepository jpaRepository;

    @Override
    public Notificacao save(Notificacao notificacao) {
        if (notificacao instanceof NotificacaoEmail notificacaoEmail) {
            NotificacaoEmailJpaEntity jpa = NotificacaoEmailJpaEntity.fromEntity(notificacaoEmail);
            return jpaRepository.save(jpa).toEntity();
        }
        throw new IllegalArgumentException("Tipo de notificação não suportado: " + notificacao.getClass().getSimpleName());
    }

    @Override
    public Optional<Notificacao> findById(UUID id) {
        return jpaRepository.findById(id).map(NotificacaoEmailJpaEntity::toEntity);
    }
}

