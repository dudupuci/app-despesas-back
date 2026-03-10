package io.github.dudupuci.appdespesas.application.usecases.movimentacao;

import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Command para criar uma movimentação.
 */
public record MovimentacaoCommand(
        String titulo,
        String descricao,
        BigDecimal valor,
        Date dataDaMovimentacao,
        TipoMovimentacao tipoMovimentacao,
        UUID categoriaId
) {}

