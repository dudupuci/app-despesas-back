package io.github.dudupuci.appdespesas.application.responses.calendario;

import io.github.dudupuci.appdespesas.domain.enums.Prioridade;
import io.github.dudupuci.appdespesas.domain.enums.TipoEvento;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Response interna do CalendarioService — sem depender de infra.
 */
public record EventoCalendarioResult(
        UUID id,
        TipoEvento tipoEvento,
        String titulo,
        String descricao,
        Date dataInicio,
        Date dataFim,
        Boolean diaInteiro,
        Prioridade prioridade,
        String cor,
        Boolean concluido,
        String localizacao,
        BigDecimal valor,
        TipoMovimentacao tipoMovimentacao,
        String categoriaNome,
        Boolean isRecorrente,
        String frequenciaRecorrencia
) {
    public static EventoCalendarioResult fromCompromisso(
            UUID id, String titulo, String descricao, Date dataInicio,
            Date dataFim, Boolean diaInteiro, Prioridade prioridade,
            String cor, Boolean concluido, String localizacao
    ) {
        return new EventoCalendarioResult(id, TipoEvento.COMPROMISSO, titulo, descricao,
                dataInicio, dataFim, diaInteiro, prioridade, cor, concluido, localizacao,
                null, null, null, null, null);
    }

    public static EventoCalendarioResult fromMovimentacaoPrevista(
            Object id, String titulo, String descricao, Date data,
            BigDecimal valor, TipoMovimentacao tipo, String categoriaNome,
            String cor, Boolean isRecorrente
    ) {
        UUID uuid = id instanceof UUID u ? u : UUID.fromString(id.toString());
        return new EventoCalendarioResult(uuid, TipoEvento.MOVIMENTACAO, titulo, descricao,
                data, data, false, null, cor, null, null,
                valor, tipo, categoriaNome, isRecorrente, null);
    }
}

