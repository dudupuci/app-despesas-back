package io.github.dudupuci.appdespesas.application.usecases.categoria;

import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;

import java.util.UUID;

/**
 * Command para criar ou atualizar uma categoria.
 */
public record CategoriaCommand(
        UUID usuarioId,
        UUID categoriaId, // usado apenas no update
        String nome,
        String descricao,
        TipoMovimentacao tipoMovimentacao,
        UUID corId
) {}
