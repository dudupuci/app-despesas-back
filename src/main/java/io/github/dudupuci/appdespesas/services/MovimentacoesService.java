package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.config.persistence.Transacional;
import io.github.dudupuci.appdespesas.controllers.dtos.CriarMovimentacaoDto;
import io.github.dudupuci.appdespesas.exceptions.*;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.repositories.MovimentacoesRepository;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;
import org.springframework.stereotype.Service;

@Service
public class MovimentacoesService {

    private final MovimentacoesRepository repository;
    private final CategoriasService categoriasService;

    public MovimentacoesService(MovimentacoesRepository repository, CategoriasService categoriasService) {
        this.repository = repository;
        this.categoriasService = categoriasService;
    }

    public Movimentacao buscarPorId(Long id) throws EntityNotFoundException {
        return this.repository.buscarPorId(id);
    }

    @Transacional
    public Movimentacao criarMovimentacao(CriarMovimentacaoDto dto) {
        Movimentacao movimentacao;
        Categoria tempCategoria;

        try {
            tempCategoria = this.categoriasService.validarCategoriaPorId(dto.categoriaId());
            movimentacao = dto.toMovimentacao();
            movimentacao.setCategoria(tempCategoria);

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
