package io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.responses.categoria;

import io.github.dudupuci.appdespesas.domain.entities.Categoria;

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
