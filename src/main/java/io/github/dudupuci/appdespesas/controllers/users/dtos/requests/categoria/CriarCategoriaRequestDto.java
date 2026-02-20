package io.github.dudupuci.appdespesas.controllers.users.dtos.requests.categoria;

import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CriarCategoriaRequestDto(
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        String descricao,
        @NotNull(message = "Tipo de movimentação é obrigatório")
        TipoMovimentacao tipoMovimentacao,
        UUID corId  // ID da cor associada (opcional)
) {

    public Categoria toCategoria() {
      return new Categoria(nome, descricao, tipoMovimentacao);
    }
}
