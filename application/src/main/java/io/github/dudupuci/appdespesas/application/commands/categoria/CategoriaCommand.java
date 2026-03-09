package io.github.dudupuci.appdespesas.application.commands.categoria;

import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;

import java.util.UUID;

/**
 * Command para criar ou atualizar uma categoria.
 */
public record CategoriaCommand(
        String nome,
        String descricao,
        TipoMovimentacao tipoMovimentacao,
        UUID corId
) {}
