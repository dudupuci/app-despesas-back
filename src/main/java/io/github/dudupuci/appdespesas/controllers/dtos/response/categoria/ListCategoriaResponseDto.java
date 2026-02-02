package io.github.dudupuci.appdespesas.controllers.dtos.response.categoria;

import io.github.dudupuci.appdespesas.models.entities.Categoria;

public record ListCategoriaResponseDto(
        Long id,
        String nome,
        String descricao
) {

    public static ListCategoriaResponseDto toDto(Categoria categoria) {
        return new ListCategoriaResponseDto(
                categoria.getId(),
                categoria.getNome(),
                categoria.getDescricao()
        );
    }
}
