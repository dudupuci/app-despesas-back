package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.AdministradorJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdministradorJpaRepository extends JpaRepository<AdministradorJpaEntity, UUID> {

    /**
     * Busca administrador por email
     */
    @Query("SELECT a FROM AdministradorJpaEntity a WHERE a.email = :email")
    Optional<AdministradorJpaEntity> buscarPorEmail(@Param("email") String email);

    /**
     * Verifica se existe administrador com o email
     */
    boolean existsByEmail(String email);
}
