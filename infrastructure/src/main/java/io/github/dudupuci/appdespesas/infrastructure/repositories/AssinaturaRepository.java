package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaAssinatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssinaturaRepository extends JpaRepository<JpaAssinatura, Long> {

    @Query("SELECT a FROM JpaAssinatura a WHERE a.nomePlano = :nomePlano")
    JpaAssinatura buscarPorNomePlano(String nomePlano);

    @Query("SELECT a FROM JpaAssinatura a where a.valor <= 0")
    JpaAssinatura buscarAssinaturaGratuita();

    @Query("SELECT a FROM JpaAssinatura a JOIN JpaUsuarioSistema u ON u.assinatura.id = a.id WHERE u.id = :usuarioId")
    Optional<JpaAssinatura> buscarAssinaturaByUsuarioId(UUID usuarioId);

    @Query("SELECT a FROM JpaAssinatura a WHERE a.id NOT IN (SELECT u.assinatura.id FROM JpaUsuarioSistema u WHERE u.id = :usuarioId)")
    Optional<List<JpaAssinatura>> buscarOutrasAssinaturasByUsuarioId(UUID usuarioId);
}
