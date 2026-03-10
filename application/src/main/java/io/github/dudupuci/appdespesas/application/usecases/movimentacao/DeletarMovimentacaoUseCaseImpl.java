package io.github.dudupuci.appdespesas.application.usecases.movimentacao;

import io.github.dudupuci.appdespesas.application.ports.repositories.MovimentacaoRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.MovimentacaoService;
import io.github.dudupuci.appdespesas.application.usecases.base.DeleteCommand;
import io.github.dudupuci.appdespesas.application.usecases.base.DeleteLongCommand;
import io.github.dudupuci.appdespesas.domain.entities.Movimentacao;

public class DeletarMovimentacaoUseCaseImpl extends DeletarMovimentacaoUseCase {

    private final MovimentacaoRepositoryPort repository;
    private final MovimentacaoService movimentacaoService;

    public DeletarMovimentacaoUseCaseImpl(MovimentacaoRepositoryPort repository,
                                          MovimentacaoService movimentacaoService) {
        this.repository = repository;
        this.movimentacaoService = movimentacaoService;
    }

    @Override
    public void executar(DeleteCommand cmd) {
        Movimentacao movimentacao = movimentacaoService.buscarPorId(cmd.id());
        if (!movimentacao.getUsuarioSistema().getId().equals(cmd.usuarioId()))
            throw new RuntimeException("Sem permissão para deletar esta movimentação");
        repository.delete(movimentacao);
    }
}
