package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.ports.repositories.MovimentacaoRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Movimentacao;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.domain.exceptions.EntityNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MovimentacaoService {

    private final MovimentacaoRepositoryPort repository;

    public MovimentacaoService(MovimentacaoRepositoryPort repository) {
        this.repository = repository;
    }

    public Movimentacao buscarPorId(UUID id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimentação com ID " + id + " não encontrada"));
    }

    public List<Movimentacao> listarTodasPorUsuarioId(UUID usuarioId, TipoMovimentacao tipoMovimentacao,
                                                       Date dataInicio, Date dataFim) {
        if (tipoMovimentacao != null && dataInicio != null && dataFim != null) {
            return this.repository.listarPorUsuarioIdTipoEPeriodo(usuarioId, tipoMovimentacao, dataInicio, dataFim);
        } else if (tipoMovimentacao != null) {
            return this.repository.listarPorUsuarioIdETipo(usuarioId, tipoMovimentacao);
        } else if (dataInicio != null && dataFim != null) {
            return this.repository.listarPorUsuarioIdEPeriodo(usuarioId, dataInicio, dataFim);
        } else {
            return this.repository.listarTodasPorUsuarioId(usuarioId);
        }
    }
}
