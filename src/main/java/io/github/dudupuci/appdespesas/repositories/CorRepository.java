package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.models.entities.Cor;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CorRepository extends JpaRepository<Cor, UUID> {

    /**
     * Busca todas as cores de um usuário
     */
    List<Cor> findByUsuarioSistema(UsuarioSistema usuarioSistema);

    /**
     * Busca cor por nome
     */
    Optional<Cor> findByNome(String nome);

    /**
     * Lista todas as cores de um usuário
     */
    @Query("SELECT c FROM Cor c WHERE c.usuarioSistema.id = :usuarioId " +
            "ORDER BY c.dataCriacao DESC")
    List<Cor> listarTodasPorUsuarioId(@Param("usuarioId") UUID usuarioId);

    /**
     * Busca uma cor específica de um usuário pelo ID
     */
    Optional<Cor> findByIdAndUsuarioSistema(UUID id, UsuarioSistema usuarioSistema);

    /**
     * Verifica se já existe uma cor com o mesmo nome para o usuário
     */
    boolean existsByNomeAndUsuarioSistema(String nome, UsuarioSistema usuarioSistema);

    /**
     * Verifica se já existe uma cor com o mesmo código hexadecimal para o usuário
     */
    boolean existsByCodigoHexadecimalAndUsuarioSistema(String codigoHexadecimal, UsuarioSistema usuarioSistema);

    /**
     * Busca cores por nome (parcial) do usuário
     */
    List<Cor> findByUsuarioSistemaAndNomeContainingIgnoreCase(UsuarioSistema usuarioSistema, String nome);
}

