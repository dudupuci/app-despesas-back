package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.config.persistence.Transacional;
import io.github.dudupuci.appdespesas.controllers.dtos.CriarCategoriaDto;
import io.github.dudupuci.appdespesas.exceptions.*;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.repositories.CategoriasRepository;
import io.github.dudupuci.appdespesas.utils.AppDespesasConstants;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriasService {

    private final CategoriasRepository repository;

    public CategoriasService(CategoriasRepository repository) {
        this.repository = repository;
    }

    public List<Categoria> listarCategoriasBySearch(String search) {
        return this.repository.listarCategoriasBySearch(search);
    }

    @Transacional
    public Categoria createCategoria(CriarCategoriaDto dto) {
        try {
            validarCriacao(dto);
            Categoria categoria = dto.toCategoria();
            this.repository.salvar(categoria);
            return categoria;
        } catch (CategoriaJaExisteException err) {
            throw new CategoriaJaExisteException(AppDespesasMessages.getMessage(
                    "categoria.ja.existente",
                    new Object[]{dto.nome()})
            );
        }
    }

    public Categoria buscarPorId(Long id) throws EntityNotFoundException {
        return this.repository.buscarPorId(id);
    }

    public boolean isCategoriaAtiva(Categoria categoria) {
        return AppDespesasConstants.ATIVO.equalsIgnoreCase(categoria.getStatus().getNome());
    }

    private void validarCriacao(CriarCategoriaDto dto) {
        Optional<Categoria> categoria = this.repository.buscarPorNome(dto.nome());

        if (categoria.isPresent()) {
            throw new CategoriaJaExisteException("Categoria com o nome '" + dto.nome() + "' já existe.");
        }

    }

    public Categoria validarCategoriaPorId(Long categoriaId) throws EntityNotFoundException, CategoriaInativaException {
        Categoria categoria = buscarPorId(categoriaId);

        if (!isCategoriaAtiva(categoria)) {
            throw new CategoriaInativaException(AppDespesasMessages.getMessage(
                    "categoria.inativa",
                    new Object[]{categoria.getNome()})
            );
        }

        return categoria;
    }

    @Transacional
    public void deletar(Long id) {
        try {
            Categoria categoria = buscarPorId(id);

            if (AppDespesasUtils.isEntidadeNotNull(categoria)) {
                this.repository.deletar(categoria);
            }
        } catch (EntityNotFoundException e) {
            throw new CategoriaNotFoundException(e.getMessage());
        }
    }
}
