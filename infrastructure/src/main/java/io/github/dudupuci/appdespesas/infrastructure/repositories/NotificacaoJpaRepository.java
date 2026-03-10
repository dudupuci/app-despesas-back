package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.NotificacaoEmailJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificacaoJpaRepository extends JpaRepository<NotificacaoEmailJpaEntity, UUID> {

}
