package io.github.dudupuci.appdespesas.controllers.dtos.request.categoria;

import io.github.dudupuci.appdespesas.models.entities.Categoria;

public record CriarCategoriaRequestDto(
        String nome,
        String descricao
) {

    public Categoria toCategoria() {
      return new Categoria(nome, descricao);
    }
}
