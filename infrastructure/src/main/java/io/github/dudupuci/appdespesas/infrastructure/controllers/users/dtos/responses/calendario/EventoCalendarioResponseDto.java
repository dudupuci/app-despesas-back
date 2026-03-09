package io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.responses.calendario;

import io.github.dudupuci.appdespesas.application.responses.calendario.EventoCalendarioResult;
import io.github.dudupuci.appdespesas.domain.enums.Prioridade;
import io.github.dudupuci.appdespesas.domain.enums.TipoEvento;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * DTO unificado que representa qualquer item do calendário
 * (Compromisso, Movimentação Prevista ou Evento Recorrente)
 */
public record EventoCalendarioResponseDto(
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

        // Campos específicos de Movimentação
        BigDecimal valor,
        TipoMovimentacao tipoMovimentacao,
        String categoriaNome,

        // Campos específicos de Evento Recorrente
        Boolean isRecorrente,
        String frequenciaRecorrencia
) {

    /**
     * Construtor para Compromissos
     */
    public static EventoCalendarioResponseDto fromCompromisso(
            UUID id, String titulo, String descricao, Date dataInicio,
            Date dataFim, Boolean diaInteiro, Prioridade prioridade,
            String cor, Boolean concluido, String localizacao
    ) {
        return new EventoCalendarioResponseDto(
                id,
                TipoEvento.COMPROMISSO,
                titulo,
                descricao,
                dataInicio,
                dataFim,
                diaInteiro,
                prioridade,
                cor,
                concluido,
                localizacao,
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * Construtor para Movimentações Previstas
     */
    public static EventoCalendarioResponseDto fromResult(EventoCalendarioResult result) {
        return new EventoCalendarioResponseDto(
                result.id(), result.tipoEvento(), result.titulo(), result.descricao(),
                result.dataInicio(), result.dataFim(), result.diaInteiro(), result.prioridade(),
                result.cor(), result.concluido(), result.localizacao(), result.valor(),
                result.tipoMovimentacao(), result.categoriaNome(), result.isRecorrente(),
                result.frequenciaRecorrencia()
        );
    }

}

