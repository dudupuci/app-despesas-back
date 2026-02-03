package io.github.dudupuci.appdespesas.controllers.dtos.request.movimentacao;

import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.models.enums.TipoPeriodo;

import java.util.Date;

/**
 * DTO para filtrar movimentações por período
 */
public record FiltroMovimentacaoRequestDto(
        TipoMovimentacao tipoMovimentacao,
        TipoPeriodo tipoPeriodo, // DIA, SEMANA, MES
        Date dataInicio,
        Date dataFim
) {
}

