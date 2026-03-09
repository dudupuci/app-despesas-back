package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaUsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuariosRepository extends JpaRepository<JpaUsuarioSistema, UUID> {

    /**
     * Busca usuário por email
     */
    @Query("SELECT u FROM JpaUsuarioSistema u WHERE u.contato.email = :email")
    Optional<JpaUsuarioSistema> buscarPorEmail(@Param("email") String email);

    /**
     * Busca usuário por nome de usuário
     */
    @Query("SELECT u FROM JpaUsuarioSistema u WHERE u.nomeUsuario = :nomeUsuario")
    Optional<JpaUsuarioSistema> buscarPorNomeUsuario(@Param("nomeUsuario") String nomeUsuario);

    /**
     * Verifica se existe usuário com o email (campo dentro de contato)
     */
    boolean existsByContatoEmail(String email);

    /**
     * Verifica se existe usuário com o nome de usuário
     */
    boolean existsByNomeUsuario(String nomeUsuario);
}
