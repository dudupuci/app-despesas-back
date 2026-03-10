package io.github.dudupuci.appdespesas.infrastructure.repositories;

import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.MovimentacaoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface MovimentacaoJpaRepository extends JpaRepository<MovimentacaoJpaEntity, UUID> {

    /**
     * Lista todas as movimentações de um usuário com filtros opcionais
     */
    @Query("SELECT m FROM MovimentacaoJpaEntity m WHERE m.usuarioSistema.id = :usuarioId " +
           "ORDER BY m.dataDaMovimentacao DESC")
    List<MovimentacaoJpaEntity> listarTodasPorUsuarioId(@Param("usuarioId") UUID usuarioId);

    /**
     * Lista movimentações por usuário e tipo
     */
    @Query("SELECT m FROM MovimentacaoJpaEntity m WHERE m.usuarioSistema.id = :usuarioId " +
           "AND m.tipoMovimentacao = :tipoMovimentacao " +
           "ORDER BY m.dataDaMovimentacao DESC")
    List<MovimentacaoJpaEntity> listarPorUsuarioIdETipo(
            @Param("usuarioId") UUID usuarioId,
            @Param("tipoMovimentacao") TipoMovimentacao tipoMovimentacao
    );

    /**
     * Lista movimentações por usuário e período
     */
    @Query("SELECT m FROM MovimentacaoJpaEntity m WHERE m.usuarioSistema.id = :usuarioId " +
           "AND m.dataDaMovimentacao BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY m.dataDaMovimentacao DESC")
    List<MovimentacaoJpaEntity> listarPorUsuarioIdEPeriodo(
            @Param("usuarioId") UUID usuarioId,
            @Param("dataInicio") Date dataInicio,
            @Param("dataFim") Date dataFim
    );

    /**
     * Lista movimentações por usuário, tipo e período
     */
    @Query("SELECT m FROM MovimentacaoJpaEntity m WHERE m.usuarioSistema.id = :usuarioId " +
           "AND m.tipoMovimentacao = :tipoMovimentacao " +
           "AND m.dataDaMovimentacao BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY m.dataDaMovimentacao DESC")
    List<MovimentacaoJpaEntity> listarPorUsuarioIdTipoEPeriodo(
            @Param("usuarioId") UUID usuarioId,
            @Param("tipoMovimentacao") TipoMovimentacao tipoMovimentacao,
            @Param("dataInicio") Date dataInicio,
            @Param("dataFim") Date dataFim
    );

    /**
     * Busca movimentações de um dia específico
     */
    @Query("SELECT m FROM MovimentacaoJpaEntity m WHERE m.usuarioSistema.id = :usuarioId " +
           "AND CAST(m.dataDaMovimentacao AS date) = CAST(:data AS date) " +
           "ORDER BY m.dataDaMovimentacao ASC")
    List<MovimentacaoJpaEntity> buscarPorData(@Param("usuarioId") UUID usuarioId, @Param("data") Date data);
}
