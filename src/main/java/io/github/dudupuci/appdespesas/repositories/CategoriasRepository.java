package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriasRepository extends JpaRepository<Categoria, UUID> {

    /**
     * Busca categorias por busca textual (nome ou descrição)
     */
    @Query("SELECT c FROM Categoria c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Categoria> listarCategoriasBySearch(@Param("search") String search);

    /**
     * Lista todas categorias de um usuário, opcionalmente filtradas por tipo
     */
    @Query("SELECT c FROM Categoria c WHERE 1=1 " +
            "AND (c.usuarioSistema.id = :usuarioId OR c.administradorId = :administradorId) " +
            "AND (:tipoMovimentacao IS NULL OR c.tipoMovimentacao = :tipoMovimentacao) " +
            "ORDER BY c.dataCriacao DESC")
    List<Categoria> listarTodasPorUsuarioId(
            @Param("usuarioId") UUID usuarioId,
            @Param("administradorId") UUID administradorId,
            @Param("tipoMovimentacao") TipoMovimentacao tipoMovimentacao);

    /**
     * Busca categoria por nome
     */
    @Query("SELECT c FROM Categoria c WHERE c.nome = :nome")
    Optional<Categoria> buscarPorNome(@Param("nome") String nome);

    /**
     * Busca uma categoria por nome e usuário
     * Usado pelo DataInitializer para verificar se categoria padrão já existe
     */
    @Query("SELECT c FROM Categoria c WHERE c.nome = :nome AND c.usuarioSistema = :usuario")
    Categoria buscarPorNomeEUsuario(@Param("nome") String nome, @Param("usuario") UsuarioSistema usuarioSistema);

    /**
     * Busca uma categoria por nome e administrador (para dados do sistema)
     */
    @Query("SELECT c FROM Categoria c WHERE c.nome = :nome AND c.administradorId = :administradorId")
    Categoria buscarPorNomeEAdministrador(@Param("nome") String nome, @Param("administradorId") UUID administradorId);
}
