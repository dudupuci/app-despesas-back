package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.ports.repositories.CategoriaRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Categoria;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.domain.exceptions.CategoriaInativaException;
import io.github.dudupuci.appdespesas.domain.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasMessages;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasUtils;

import java.util.List;
import java.util.UUID;

public class CategoriaService {

    private final CategoriaRepositoryPort repository;
    private final UsuarioService usuarioService;

    public CategoriaService(CategoriaRepositoryPort repository, UsuarioService usuarioService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
    }

    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public List<Categoria> listarCategoriasBySearch(String search) {
        return this.repository.listarCategoriasBySearch(search);
    }

    public Categoria buscarPorId(UUID id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria com ID " + id + " não encontrada"));
    }

    public List<Categoria> listarTodasPorUsuarioId(UUID usuarioId, TipoMovimentacao tipoMovimentacao) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        return this.repository.listarTodasPorUsuarioId(usuario.getId(), tipoMovimentacao);
    }

    public Categoria validarCategoriaPorId(UUID categoriaId) {
        Categoria categoria = buscarPorId(categoriaId);
        if (!AppDespesasUtils.isCategoriaAtiva(categoria)) {
            throw new CategoriaInativaException(AppDespesasMessages.getMessage(
                    "categoria.inativa", new Object[]{categoria.getNome()}));
        }
        return categoria;
    }
}
