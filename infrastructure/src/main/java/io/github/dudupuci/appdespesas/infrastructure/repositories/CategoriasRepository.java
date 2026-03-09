package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaCategoria;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.JpaUsuarioSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriasRepository extends JpaRepository<JpaCategoria, UUID> {

    /**
     * Busca categorias por busca textual (nome ou descrição)
     */
    @Query("SELECT c FROM JpaCategoria c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<JpaCategoria> listarCategoriasBySearch(@Param("search") String search);

    /**
     * Lista todas categorias de um usuário, opcionalmente filtradas por tipo
     */
    @Query("SELECT c FROM JpaCategoria c WHERE c.usuarioSistema.id = :usuarioId " +
            "AND (:tipoMovimentacao IS NULL OR c.tipoMovimentacao = :tipoMovimentacao) " +
            "ORDER BY c.dataCriacao DESC")
    List<JpaCategoria> listarTodasPorUsuarioId(
            @Param("usuarioId") UUID usuarioId,
            @Param("tipoMovimentacao") TipoMovimentacao tipoMovimentacao);

    /**
     * Busca categoria por nome
     */
    @Query("SELECT c FROM JpaCategoria c WHERE c.nome = :nome")
    Optional<JpaCategoria> buscarPorNome(@Param("nome") String nome);

    /**
     * Busca uma categoria por nome e usuário
     */
    @Query("SELECT c FROM JpaCategoria c WHERE c.nome = :nome AND c.usuarioSistema = :usuario")
    JpaCategoria buscarPorNomeEUsuario(@Param("nome") String nome, @Param("usuario") JpaUsuarioSistema usuarioSistema);
}
