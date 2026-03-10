package io.github.dudupuci.appdespesas.application.usecases.movimentacao;

import io.github.dudupuci.appdespesas.application.ports.repositories.MovimentacaoRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.MovimentacaoService;
import io.github.dudupuci.appdespesas.domain.entities.Movimentacao;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasUtils;

public class DeletarMovimentacaoUseCaseImpl extends DeletarMovimentacaoUseCase {

    private final MovimentacaoRepositoryPort repository;
    private final MovimentacaoService movimentacaoService;

    public DeletarMovimentacaoUseCaseImpl(MovimentacaoRepositoryPort repository, MovimentacaoService movimentacaoService) {
        this.repository = repository;
        this.movimentacaoService = movimentacaoService;
    }

    @Override
    public void executar(Long id) {
        Movimentacao movimentacao = movimentacaoService.buscarPorId(id);
        if (AppDespesasUtils.isEntidadeNotNull(movimentacao)) {
            repository.delete(movimentacao);
        }
    }
}

