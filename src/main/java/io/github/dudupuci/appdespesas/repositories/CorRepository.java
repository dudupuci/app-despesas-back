package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.models.entities.Cor;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
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
     * Busca todas as cores de um usuário ordenadas por nome
     */
    List<Cor> findByUsuarioSistemaOrderByNomeAsc(UsuarioSistema usuarioSistema);

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
     * Verifica se já existe uma cor com o mesmo nome criada por administrador
     */
    boolean existsByNomeAndAdministradorId(String nome, UUID administradorId);

    /**
     * Verifica se já existe uma cor com o mesmo código hexadecimal criada por administrador
     */
    boolean existsByCodigoHexadecimalAndAdministradorId(String codigoHexadecimal, UUID administradorId);

    /**
     * Busca cores por nome (parcial) do usuário
     */
    List<Cor> findByUsuarioSistemaAndNomeContainingIgnoreCase(UsuarioSistema usuarioSistema, String nome);
}

