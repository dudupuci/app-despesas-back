package io.github.dudupuci.appdespesas.controllers.users.dtos.responses.calendario;

import io.github.dudupuci.appdespesas.models.enums.Prioridade;
import io.github.dudupuci.appdespesas.models.enums.TipoEvento;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;

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
    public static EventoCalendarioResponseDto fromMovimentacaoPrevista(
            UUID id, String titulo, String descricao, Date dataMovimentacao,
            BigDecimal valor, TipoMovimentacao tipoMovimentacao, String categoriaNome,
            String cor, Boolean isRecorrente
    ) {
        return new EventoCalendarioResponseDto(
                id,
                TipoEvento.MOVIMENTACAO,
                titulo,
                descricao,
                dataMovimentacao,
                null,
                true,
                null,
                cor,
                null,
                null,
                valor,
                tipoMovimentacao,
                categoriaNome,
                isRecorrente,
                null
        );
    }

}

