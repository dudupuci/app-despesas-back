package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.CategoriaJpaEntity;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.UsuarioSistemaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoriaJpaRepository extends JpaRepository<CategoriaJpaEntity, UUID> {

    /**
     * Busca categorias por busca textual (nome ou descrição)
     */
    @Query("SELECT c FROM CategoriaJpaEntity c WHERE LOWER(c.nome) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(c.descricao) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<CategoriaJpaEntity> listarCategoriasBySearch(@Param("search") String search);

    /**
     * Lista todas categorias de um usuário, opcionalmente filtradas por tipo
     */
    @Query("SELECT c FROM CategoriaJpaEntity c WHERE c.usuarioSistema.id = :usuarioId " +
            "AND (:tipoMovimentacao IS NULL OR c.tipoMovimentacao = :tipoMovimentacao) " +
            "ORDER BY c.dataCriacao DESC")
    List<CategoriaJpaEntity> listarTodasPorUsuarioId(
            @Param("usuarioId") UUID usuarioId,
            @Param("tipoMovimentacao") TipoMovimentacao tipoMovimentacao);

    /**
     * Busca categoria por nome
     */
    @Query("SELECT c FROM CategoriaJpaEntity c WHERE c.nome = :nome")
    Optional<CategoriaJpaEntity> buscarPorNome(@Param("nome") String nome);

    /**
     * Busca uma categoria por nome e usuário
     */
    @Query("SELECT c FROM CategoriaJpaEntity c WHERE c.nome = :nome AND c.usuarioSistema = :usuario")
    CategoriaJpaEntity buscarPorNomeEUsuario(@Param("nome") String nome, @Param("usuario") UsuarioSistemaJpaEntity usuarioSistema);
}
