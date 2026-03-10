package io.github.dudupuci.appdespesas.application.usecases.categoria;

import io.github.dudupuci.appdespesas.application.ports.repositories.CategoriaRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CategoriaService;
import io.github.dudupuci.appdespesas.application.usecases.base.DeleteCommand;
import io.github.dudupuci.appdespesas.domain.entities.Categoria;

public class DeletarCategoriaUseCaseImpl extends DeletarCategoriaUseCase {

    private final CategoriaRepositoryPort repository;
    private final CategoriaService categoriaService;

    public DeletarCategoriaUseCaseImpl(CategoriaRepositoryPort repository, CategoriaService categoriaService) {
        this.repository = repository;
        this.categoriaService = categoriaService;
    }

    @Override
    public void executar(DeleteCommand cmd) {
        Categoria categoria = categoriaService.buscarPorId(cmd.id());
        if (!categoria.getUsuarioSistema().getId().equals(cmd.usuarioId()))
            throw new RuntimeException("Você não tem permissão para deletar esta categoria");
        repository.delete(categoria);
    }
}
