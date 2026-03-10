package io.github.dudupuci.appdespesas.application.ports.repositories;

import io.github.dudupuci.appdespesas.domain.entities.Movimentacao;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port (interface) para o repositório de Movimentações
 */
public interface MovimentacaoRepositoryPort {

    Movimentacao save(Movimentacao movimentacao);

    Optional<Movimentacao> findById(UUID id);

    List<Movimentacao> listarTodasPorUsuarioId(UUID usuarioId);

    List<Movimentacao> listarPorUsuarioIdETipo(UUID usuarioId, TipoMovimentacao tipoMovimentacao);

    List<Movimentacao> listarPorUsuarioIdEPeriodo(UUID usuarioId, Date dataInicio, Date dataFim);

    List<Movimentacao> listarPorUsuarioIdTipoEPeriodo(UUID usuarioId, TipoMovimentacao tipoMovimentacao, Date dataInicio, Date dataFim);

    List<Movimentacao> buscarPorData(UUID usuarioId, Date data);

    void delete(Movimentacao movimentacao);

    void deleteById(Long id);
}

