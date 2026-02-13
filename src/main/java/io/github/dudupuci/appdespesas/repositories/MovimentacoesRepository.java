package io.github.dudupuci.appdespesas.repositories;

import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface MovimentacoesRepository extends JpaRepository<Movimentacao, Long> {

    /**
     * Lista todas as movimentações de um usuário com filtros opcionais
     */
    @Query("SELECT m FROM Movimentacao m WHERE m.usuarioSistema.id = :usuarioId " +
           "ORDER BY m.dataDaMovimentacao DESC")
    List<Movimentacao> listarTodasPorUsuarioId(@Param("usuarioId") UUID usuarioId);

    /**
     * Lista movimentações por usuário e tipo
     */
    @Query("SELECT m FROM Movimentacao m WHERE m.usuarioSistema.id = :usuarioId " +
           "AND m.tipoMovimentacao = :tipoMovimentacao " +
           "ORDER BY m.dataDaMovimentacao DESC")
    List<Movimentacao> listarPorUsuarioIdETipo(
            @Param("usuarioId") UUID usuarioId,
            @Param("tipoMovimentacao") TipoMovimentacao tipoMovimentacao
    );

    /**
     * Lista movimentações por usuário e período
     */
    @Query("SELECT m FROM Movimentacao m WHERE m.usuarioSistema.id = :usuarioId " +
           "AND m.dataDaMovimentacao BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY m.dataDaMovimentacao DESC")
    List<Movimentacao> listarPorUsuarioIdEPeriodo(
            @Param("usuarioId") UUID usuarioId,
            @Param("dataInicio") Date dataInicio,
            @Param("dataFim") Date dataFim
    );

    /**
     * Lista movimentações por usuário, tipo e período
     */
    @Query("SELECT m FROM Movimentacao m WHERE m.usuarioSistema.id = :usuarioId " +
           "AND m.tipoMovimentacao = :tipoMovimentacao " +
           "AND m.dataDaMovimentacao BETWEEN :dataInicio AND :dataFim " +
           "ORDER BY m.dataDaMovimentacao DESC")
    List<Movimentacao> listarPorUsuarioIdTipoEPeriodo(
            @Param("usuarioId") UUID usuarioId,
            @Param("tipoMovimentacao") TipoMovimentacao tipoMovimentacao,
            @Param("dataInicio") Date dataInicio,
            @Param("dataFim") Date dataFim
    );

    /**
     * Busca movimentações de um dia específico
     */
    @Query("SELECT m FROM Movimentacao m WHERE m.usuarioSistema.id = :usuarioId " +
           "AND CAST(m.dataDaMovimentacao AS date) = CAST(:data AS date) " +
           "ORDER BY m.dataDaMovimentacao ASC")
    List<Movimentacao> buscarPorData(@Param("usuarioId") UUID usuarioId, @Param("data") Date data);
}
