package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.models.entities.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, UUID> {

    /**
     * Busca administrador por email
     */
    @Query("SELECT a FROM Administrador a WHERE a.email = :email")
    Optional<Administrador> buscarPorEmail(@Param("email") String email);

    /**
     * Verifica se existe administrador com o email
     */
    boolean existsByEmail(String email);
}

