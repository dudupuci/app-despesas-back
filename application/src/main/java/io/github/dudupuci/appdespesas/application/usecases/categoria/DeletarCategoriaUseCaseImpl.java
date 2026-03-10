package io.github.dudupuci.appdespesas.application.usecases.categoria;

import io.github.dudupuci.appdespesas.application.ports.repositories.CategoriaRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CategoriaService;
import io.github.dudupuci.appdespesas.domain.entities.Categoria;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasUtils;

import java.util.UUID;

public class DeletarCategoriaUseCaseImpl extends DeletarCategoriaUseCase {

    private final CategoriaRepositoryPort repository;
    private final CategoriaService categoriaService;

    public DeletarCategoriaUseCaseImpl(CategoriaRepositoryPort repository, CategoriaService categoriaService) {
        this.repository = repository;
        this.categoriaService = categoriaService;
    }

    @Override
    public void executar(UUID id) {
        Categoria categoria = categoriaService.buscarPorId(id);
            repository.delete(categoria);

    }
}

