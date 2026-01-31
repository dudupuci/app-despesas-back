package io.github.dudupuci.appdespesas.controllers.dtos.response;

import io.github.dudupuci.appdespesas.models.entities.Categoria;

public record ListCategoriaDto(
        Long id,
        String nome,
        String descricao
) {

    public static ListCategoriaDto toDto(Categoria categoria) {
        return new ListCategoriaDto(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao()
        );
    }
}
