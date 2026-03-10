package io.github.dudupuci.appdespesas.application.usecases.categoria;

import io.github.dudupuci.appdespesas.application.ports.repositories.CategoriaRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CategoriaService;
import io.github.dudupuci.appdespesas.application.services.CorService;
import io.github.dudupuci.appdespesas.domain.entities.Categoria;
import io.github.dudupuci.appdespesas.domain.entities.Cor;

import java.util.UUID;

public class AtualizarCategoriaUseCaseImpl extends AtualizarCategoriaUseCase {

    private final CategoriaRepositoryPort repository;
    private final CategoriaService categoriaService;
    private final CorService corService;
    private final UUID id;
    private final UUID usuarioId;

    public AtualizarCategoriaUseCaseImpl(CategoriaRepositoryPort repository, CategoriaService categoriaService,
                                         CorService corService, UUID id, UUID usuarioId) {
        this.repository = repository;
        this.categoriaService = categoriaService;
        this.corService = corService;
        this.id = id;
        this.usuarioId = usuarioId;
    }

    @Override
    public Categoria executar(CategoriaCommand cmd) {
        Categoria categoria = categoriaService.buscarPorId(id);
        if (!categoria.getUsuarioSistema().getId().equals(usuarioId)) {
            throw new RuntimeException("Você não tem permissão para editar esta categoria");
        }
        categoria.setNome(cmd.nome());
        categoria.setDescricao(cmd.descricao());
        categoria.setTipoMovimentacao(cmd.tipoMovimentacao());
        if (cmd.corId() != null) {
            Cor cor = corService.buscarPorId(cmd.corId(), usuarioId);
            categoria.setCor(cor);
        }
        return repository.save(categoria);
    }
}

