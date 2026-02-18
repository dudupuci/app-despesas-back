package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UsuariosRepository extends JpaRepository<UsuarioSistema, UUID> {

    /**
     * Busca usuário por email
     */
    @Query("SELECT u FROM UsuarioSistema u WHERE u.contato.email = :email")
    Optional<UsuarioSistema> buscarPorEmail(@Param("email") String email);

    /**
     * Busca usuário por nome de usuário
     */
    @Query("SELECT u FROM UsuarioSistema u WHERE u.nomeUsuario = :nomeUsuario")
    Optional<UsuarioSistema> buscarPorNomeUsuario(@Param("nomeUsuario") String nomeUsuario);

    /**
     * Verifica se existe usuário com o email (campo dentro de contato)
     */
    boolean existsByContatoEmail(String email);

    /**
     * Verifica se existe usuário com o nome de usuário
     */
    boolean existsByNomeUsuario(String nomeUsuario);
}
