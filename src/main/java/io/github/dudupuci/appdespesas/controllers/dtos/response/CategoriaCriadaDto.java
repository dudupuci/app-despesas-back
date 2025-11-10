package io.github.dudupuci.appdespesas.controllers.dtos.response;

import io.github.dudupuci.appdespesas.models.entities.Categoria;

public record CategoriaCriadaDto(
        String nome,
        String descricao
)  {

    public static CategoriaCriadaDto fromCategoria(Categoria categoria) {
        return new CategoriaCriadaDto(
                categoria.getNome(),
                categoria.getDescricao()
        );
    }
}
