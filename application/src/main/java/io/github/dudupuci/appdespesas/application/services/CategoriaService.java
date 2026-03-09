package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.commands.categoria.CategoriaCommand;
import io.github.dudupuci.appdespesas.application.ports.repositories.CategoriaRepositoryPort;
import jakarta.transaction.Transactional;
import io.github.dudupuci.appdespesas.domain.exceptions.CategoriaInativaException;
import io.github.dudupuci.appdespesas.domain.exceptions.EntityAlreadyExistsException;
import io.github.dudupuci.appdespesas.domain.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.domain.entities.Categoria;
import io.github.dudupuci.appdespesas.domain.entities.Cor;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasMessages;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoriaService {

    private final CategoriaRepositoryPort repository;
    private final UsuarioService usuarioService;
    private final CorService corService;

    public CategoriaService(CategoriaRepositoryPort repository, UsuarioService usuarioService, CorService corService) {
        this.repository = repository;
        this.usuarioService = usuarioService;
        this.corService = corService;
    }

    public List<Categoria> listarCategoriasBySearch(String search) {
        return this.repository.listarCategoriasBySearch(search);
    }

    @Transactional
    public Categoria createCategoria(CategoriaCommand cmd, UUID usuarioId) {
        validarCriacao(cmd.nome());
        UsuarioSistema usuarioSistema = this.usuarioService.buscarPorId(usuarioId);

        Categoria categoria = new Categoria(cmd.nome(), cmd.descricao(), cmd.tipoMovimentacao());
        categoria.setUsuarioSistema(usuarioSistema);

        if (cmd.corId() != null) {
            Cor cor = corService.buscarPorId(cmd.corId(), usuarioId);
            categoria.setCor(cor);
        }

        return this.repository.save(categoria);
    }

    @Transactional
    public Categoria updateCategoria(UUID id, CategoriaCommand cmd, UUID usuarioId) {
        Categoria categoria = buscarPorId(id);

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

        return this.repository.save(categoria);
    }

    public Categoria buscarPorId(UUID id) {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria com ID " + id + " não encontrada"));
    }

    public List<Categoria> listarTodasPorUsuarioId(UUID usuarioId, TipoMovimentacao tipoMovimentacao) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        return this.repository.listarTodasPorUsuarioId(usuario.getId(), tipoMovimentacao);
    }

    private void validarCriacao(String nome) {
        Optional<Categoria> categoria = this.repository.buscarPorNome(nome);
        if (categoria.isPresent()) {
            throw new EntityAlreadyExistsException("Categoria com o nome '" + nome + "' já existe.");
        }
    }

    public Categoria validarCategoriaPorId(UUID categoriaId) throws EntityNotFoundException, CategoriaInativaException {
        Categoria categoria = buscarPorId(categoriaId);
        if (!AppDespesasUtils.isCategoriaAtiva(categoria)) {
            throw new CategoriaInativaException(AppDespesasMessages.getMessage(
                    "categoria.inativa", new Object[]{categoria.getNome()}));
        }
        return categoria;
    }

    @Transactional
    public void deletar(UUID id) {
        Categoria categoria = buscarPorId(id);
        if (AppDespesasUtils.isEntidadeNotNull(categoria)) {
            this.repository.delete(categoria);
        }
    }
}
