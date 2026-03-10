package io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.categoria;

import io.github.dudupuci.appdespesas.application.usecases.categoria.CategoriaCommand;
import io.github.dudupuci.appdespesas.domain.entities.Categoria;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CriarCategoriaRequestDto(
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        String descricao,
        @NotNull(message = "Tipo de movimentação é obrigatório")
        TipoMovimentacao tipoMovimentacao,
        UUID corId
) {
    public CategoriaCommand toCommand(UUID usuarioId, UUID categoriaId) {
        return new CategoriaCommand(usuarioId, categoriaId, nome, descricao, tipoMovimentacao, corId);
    }

    /** @deprecated use toCommand(usuarioId, categoriaId) */
    public Categoria toCategoria() {
        return new Categoria(nome, descricao, tipoMovimentacao);
    }
}
