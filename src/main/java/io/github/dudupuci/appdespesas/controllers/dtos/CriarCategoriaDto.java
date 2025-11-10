package io.github.dudupuci.appdespesas.controllers.dtos;

import io.github.dudupuci.appdespesas.models.entities.Categoria;

public record CriarCategoriaDto(
        String nome,
        String descricao
) {

    public Categoria toCategoria() {
      return new Categoria(nome, descricao);
    }
}
