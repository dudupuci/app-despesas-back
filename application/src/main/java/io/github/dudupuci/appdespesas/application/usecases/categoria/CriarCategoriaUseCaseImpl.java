package io.github.dudupuci.appdespesas.application.usecases.categoria;

import io.github.dudupuci.appdespesas.application.ports.repositories.CategoriaRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CorService;
import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.domain.entities.Categoria;
import io.github.dudupuci.appdespesas.domain.entities.Cor;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.exceptions.EntityAlreadyExistsException;

import java.util.UUID;

public class CriarCategoriaUseCaseImpl extends CriarCategoriaUseCase {

    private final CategoriaRepositoryPort repository;
    private final UsuarioService usuarioService;
    private final CorService corService;
    private final UUID usuarioId;

    public CriarCategoriaUseCaseImpl(CategoriaRepositoryPort repository, UsuarioService usuarioService,
                                     CorService corService, UUID usuarioId) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.corService = corService;
        this.usuarioId = usuarioId;
    }

    @Override
    public Categoria executar(CategoriaCommand cmd) {
        if (repository.buscarPorNome(cmd.nome()).isPresent()) {
            throw new EntityAlreadyExistsException("Categoria com o nome '" + cmd.nome() + "' já existe.");
        }
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        Categoria categoria = new Categoria(cmd.nome(), cmd.descricao(), cmd.tipoMovimentacao());
        categoria.setUsuarioSistema(usuario);
        if (cmd.corId() != null) {
            Cor cor = corService.buscarPorId(cmd.corId(), usuarioId);
            categoria.setCor(cor);
        }
        return repository.save(categoria);
    }
}

