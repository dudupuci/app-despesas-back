package io.github.dudupuci.appdespesas.controllers.dtos.response.categoria;

import io.github.dudupuci.appdespesas.models.entities.Categoria;

import java.util.UUID;

public record ListCategoriaResponseDto(
        UUID id,
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
