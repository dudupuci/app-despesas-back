package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.ports.repositories.MovimentacaoRepositoryPort;
import io.github.dudupuci.appdespesas.application.ports.repositories.UsuarioRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Categoria;
import io.github.dudupuci.appdespesas.domain.entities.Movimentacao;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.domain.exceptions.CategoriaInativaException;
import io.github.dudupuci.appdespesas.domain.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.domain.exceptions.UsuarioNotFoundException;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovimentacaoService {

    private final MovimentacaoRepositoryPort repository;
    private final CategoriaService categoriaService;
    private final UsuarioRepositoryPort usuariosRepository;

    public MovimentacaoService(
            MovimentacaoRepositoryPort repository,
            CategoriaService categoriaService,
            UsuarioRepositoryPort usuariosRepository
    ) {
        this.repository = repository;
        this.categoriaService = categoriaService;
        this.usuariosRepository = usuariosRepository;
    }

    public Movimentacao buscarPorId(UUID id) throws EntityNotFoundException {
        return this.repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movimentação com ID " + id + " não encontrada"));
    }

    public List<Movimentacao> listarTodasPorUsuarioId(
            UUID usuarioId,
            TipoMovimentacao tipoMovimentacao,
            Date dataInicio,
            Date dataFim
    ) {
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

    public Movimentacao criarMovimentacao(io.github.dudupuci.appdespesas.application.usecases.movimentacao.MovimentacaoCommand cmd, UUID usuarioId) {
        try {
            Categoria tempCategoria = this.categoriaService.validarCategoriaPorId(cmd.categoriaId());

            Optional<UsuarioSistema> usuario = this.usuariosRepository.findById(usuarioId);
            if (usuario.isEmpty()) {
                throw new UsuarioNotFoundException("Usuário não encontrado");
            }

            Movimentacao movimentacao = new Movimentacao();
            movimentacao.setTitulo(cmd.titulo());
            movimentacao.setDescricao(cmd.descricao());
            movimentacao.setValor(cmd.valor());
            movimentacao.setDataDaMovimentacao(cmd.dataDaMovimentacao());
            movimentacao.setTipoMovimentacao(cmd.tipoMovimentacao());
            movimentacao.setCategoria(tempCategoria);
            movimentacao.setUsuarioSistema(usuario.get());

            return repository.save(movimentacao);

        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException(e.getMessage());
        } catch (CategoriaInativaException e) {
            throw new CategoriaInativaException(e.getMessage());
        }
    }

    public void deletar(UUID id) {
        Movimentacao movimentacao = buscarPorId(id);
        if (AppDespesasUtils.isEntidadeNotNull(movimentacao)) {
            this.repository.delete(movimentacao);
        }
    }
}
