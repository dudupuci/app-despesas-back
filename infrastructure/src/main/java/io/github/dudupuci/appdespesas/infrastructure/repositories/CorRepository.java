package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaCor;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaUsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CorRepository extends JpaRepository<JpaCor, UUID> {

    /**
     * Busca todas as cores de um usuário
     */
    List<JpaCor> findByUsuarioSistema(JpaUsuarioSistema usuarioSistema);

    /**
     * Busca cor por nome
     */
    Optional<JpaCor> findByNome(String nome);

    /**
     * Lista todas as cores de um usuário
     */
    @Query("SELECT c FROM JpaCor c WHERE c.usuarioSistema.id = :usuarioId " +
            "ORDER BY c.dataCriacao DESC")
    List<JpaCor> listarTodasPorUsuarioId(@Param("usuarioId") UUID usuarioId);

    /**
     * Busca uma cor específica de um usuário pelo ID
     */
    Optional<JpaCor> findByIdAndUsuarioSistema(UUID id, JpaUsuarioSistema usuarioSistema);

    /**
     * Verifica se já existe uma cor com o mesmo nome para o usuário
     */
    boolean existsByNomeAndUsuarioSistema(String nome, JpaUsuarioSistema usuarioSistema);

    /**
     * Verifica se já existe uma cor com o mesmo código hexadecimal para o usuário
     */
    boolean existsByCodigoHexadecimalAndUsuarioSistema(String codigoHexadecimal, JpaUsuarioSistema usuarioSistema);

    /**
     * Busca cores por nome (parcial) do usuário
     */
    List<JpaCor> findByUsuarioSistemaAndNomeContainingIgnoreCase(JpaUsuarioSistema usuarioSistema, String nome);
}

