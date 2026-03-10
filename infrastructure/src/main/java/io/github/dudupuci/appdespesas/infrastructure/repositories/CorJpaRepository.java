package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.CorJpaEntity;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.UsuarioSistemaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CorJpaRepository extends JpaRepository<CorJpaEntity, UUID> {

    /**
     * Busca todas as cores de um usuário
     */
    List<CorJpaEntity> findByUsuarioSistema(UsuarioSistemaJpaEntity usuarioSistema);

    /**
     * Busca cor por nome
     */
    Optional<CorJpaEntity> findByNome(String nome);

    /**
     * Lista todas as cores de um usuário
     */
    @Query("SELECT c FROM CorJpaEntity c WHERE c.usuarioSistema.id = :usuarioId " +
            "ORDER BY c.dataCriacao DESC")
    List<CorJpaEntity> listarTodasPorUsuarioId(@Param("usuarioId") UUID usuarioId);

    /**
     * Busca uma cor específica de um usuário pelo ID
     */
    Optional<CorJpaEntity> findByIdAndUsuarioSistema(UUID id, UsuarioSistemaJpaEntity usuarioSistema);

    /**
     * Verifica se já existe uma cor com o mesmo nome para o usuário
     */
    boolean existsByNomeAndUsuarioSistema(String nome, UsuarioSistemaJpaEntity usuarioSistema);

    /**
     * Verifica se já existe uma cor com o mesmo código hexadecimal para o usuário
     */
    boolean existsByCodigoHexadecimalAndUsuarioSistema(String codigoHexadecimal, UsuarioSistemaJpaEntity usuarioSistema);

    /**
     * Busca cores por nome (parcial) do usuário
     */
    List<CorJpaEntity> findByUsuarioSistemaAndNomeContainingIgnoreCase(UsuarioSistemaJpaEntity usuarioSistema, String nome);
}

