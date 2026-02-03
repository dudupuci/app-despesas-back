package io.github.dudupuci.appdespesas.controllers.dtos.request.categoria;

import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarCategoriaRequestDto(
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        String descricao,
        @NotNull(message = "Tipo de movimentação é obrigatório")
        TipoMovimentacao tipoMovimentacao
) {

    public Categoria toCategoria() {
      return new Categoria(nome, descricao, tipoMovimentacao);
    }
}
