package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaAdministrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdministradorRepository extends JpaRepository<JpaAdministrador, UUID> {

    /**
     * Busca administrador por email
     */
    @Query("SELECT a FROM JpaAdministrador a WHERE a.email = :email")
    Optional<JpaAdministrador> buscarPorEmail(@Param("email") String email);

    /**
     * Verifica se existe administrador com o email
     */
    boolean existsByEmail(String email);
}
