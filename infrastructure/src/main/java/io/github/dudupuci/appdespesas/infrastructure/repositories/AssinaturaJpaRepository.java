package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.AssinaturaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssinaturaJpaRepository extends JpaRepository<AssinaturaJpaEntity, Long> {

    @Query("SELECT a FROM AssinaturaJpaEntity a WHERE a.nomePlano = :nomePlano")
    AssinaturaJpaEntity buscarPorNomePlano(String nomePlano);

    @Query("SELECT a FROM AssinaturaJpaEntity a where a.valor <= 0")
    AssinaturaJpaEntity buscarAssinaturaGratuita();

    @Query("SELECT a FROM AssinaturaJpaEntity a JOIN UsuarioSistemaJpaEntity u ON u.assinatura.id = a.id WHERE u.id = :usuarioId")
    Optional<AssinaturaJpaEntity> buscarAssinaturaByUsuarioId(UUID usuarioId);

    @Query("SELECT a FROM AssinaturaJpaEntity a WHERE a.id NOT IN (SELECT u.assinatura.id FROM UsuarioSistemaJpaEntity u WHERE u.id = :usuarioId)")
    Optional<List<AssinaturaJpaEntity>> buscarOutrasAssinaturasByUsuarioId(UUID usuarioId);
}
