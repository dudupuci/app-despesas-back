package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.config.persistence.Transacional;
import io.github.dudupuci.appdespesas.controllers.dtos.request.movimentacao.CriarMovimentacaoRequestDto;
import io.github.dudupuci.appdespesas.exceptions.*;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.repositories.MovimentacoesRepository;
import io.github.dudupuci.appdespesas.repositories.UsuariosRepository;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MovimentacoesService {

    private final MovimentacoesRepository repository;
    private final CategoriasService categoriasService;
    private final UsuariosRepository usuariosRepository;

    public MovimentacoesService(
            MovimentacoesRepository repository,
            CategoriasService categoriasService,
            UsuariosRepository usuariosRepository
    ) {
        this.repository = repository;
        this.categoriasService = categoriasService;
        this.usuariosRepository = usuariosRepository;
    }

    public Movimentacao buscarPorId(Long id) throws EntityNotFoundException {
        return this.repository.buscarPorId(id);
    }

    public List<Movimentacao> listarTodasPorUsuarioId(UUID usuarioId, TipoMovimentacao tipoMovimentacao) {
        return this.repository.listarTodasPorUsuarioId(usuarioId, tipoMovimentacao);
    }

    @Transacional
    public Movimentacao criarMovimentacao(CriarMovimentacaoRequestDto dto, UUID usuarioId) {
        Movimentacao movimentacao;
        Categoria tempCategoria;
        UsuarioSistema usuario;

        try {
            // Buscar e validar categoria
            tempCategoria = this.categoriasService.validarCategoriaPorId(dto.categoriaId());

            // Buscar usuário
            usuario = this.usuariosRepository.buscarPorId(usuarioId);
            if (usuario == null) {
                throw new UsuarioNotFoundException("Usuário não encontrado");
            }

            // Criar movimentação
            movimentacao = dto.toMovimentacao();
            movimentacao.setCategoria(tempCategoria);
            movimentacao.setUsuarioSistema(usuario);
            repository.salvar(movimentacao);

        } catch (EntityNotFoundException e) {
            throw new CategoriaNotFoundException(e.getMessage());
        } catch (CategoriaInativaException e) {
            throw new CategoriaInativaException(e.getMessage());
        }

        return movimentacao;
    }

    @Transacional
    public void deletar(Long id) {
        try {
            Movimentacao movimentacao = buscarPorId(id);

            if (AppDespesasUtils.isEntidadeNotNull(movimentacao)) {
                this.repository.deletar(movimentacao);
            }
        } catch (EntityNotFoundException e) {
            throw new MovimentacaoNotFoundException(e.getMessage());
        }
    }
}
