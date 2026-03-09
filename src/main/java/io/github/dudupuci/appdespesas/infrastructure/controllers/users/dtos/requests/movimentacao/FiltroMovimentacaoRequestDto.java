package io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.movimentacao;

import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.domain.enums.TipoPeriodo;

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

