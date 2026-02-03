package io.github.dudupuci.appdespesas.controllers.dtos.request.categoria;

import io.github.dudupuci.appdespesas.models.entities.Categoria;
import jakarta.validation.constraints.NotBlank;

public record CriarCategoriaRequestDto(
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        String descricao
) {

    public Categoria toCategoria() {
      return new Categoria(nome, descricao);
    }
}
