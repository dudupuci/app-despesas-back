package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssinaturaRepository extends JpaRepository<Assinatura, Long> {

    @Query("SELECT a FROM Assinatura a WHERE a.nomePlano = :nomePlano")
    Assinatura buscarPorNomePlano(String nomePlano);

    @Query("SELECT a FROM Assinatura a where a.valor <= 0")
    Assinatura buscarAssinaturaGratuita();

    @Query("SELECT a FROM Assinatura a JOIN UsuarioSistema u ON u.assinatura.id = a.id WHERE u.id = :usuarioId")
    Optional<Assinatura> buscarAssinaturaByUsuarioId(UUID usuarioId);

    @Query("SELECT a FROM Assinatura a WHERE a.id NOT IN (SELECT u.assinatura.id FROM UsuarioSistema u WHERE u.id = :usuarioId)")
    Optional<List<Assinatura>> buscarOutrasAssinaturasByUsuarioId(UUID usuarioId);
}
