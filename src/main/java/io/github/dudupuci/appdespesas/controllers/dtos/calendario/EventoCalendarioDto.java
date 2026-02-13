package io.github.dudupuci.appdespesas.controllers.dtos.calendario;

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
public record EventoCalendarioDto(
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
    public static EventoCalendarioDto fromCompromisso(
            UUID id, String titulo, String descricao, Date dataInicio,
            Date dataFim, Boolean diaInteiro, Prioridade prioridade,
            String cor, Boolean concluido, String localizacao
    ) {
        return new EventoCalendarioDto(
            id, TipoEvento.COMPROMISSO, titulo, descricao, dataInicio, dataFim,
            diaInteiro, prioridade, cor, concluido, localizacao,
            null, null, null, null, null
        );
    }

    /**
     * Construtor para Movimentações Previstas
     */
    public static EventoCalendarioDto fromMovimentacaoPrevista(
        UUID id, String titulo, String descricao, Date dataMovimentacao,
        BigDecimal valor, TipoMovimentacao tipoMovimentacao, String categoriaNome,
        String cor, Boolean isRecorrente
    ) {
        return new EventoCalendarioDto(
            id, TipoEvento.MOVIMENTACAO_PREVISTA, titulo, descricao,
            dataMovimentacao, null, true, Prioridade.MEDIA, cor,
            null, null,
            valor, tipoMovimentacao, categoriaNome,
            isRecorrente, null
        );
    }

    /**
     * Construtor para Eventos Recorrentes
     */
    public static EventoCalendarioDto fromEventoRecorrente(
        UUID id, String titulo, String descricao, Date dataInicio,
        Date dataFim, Boolean diaInteiro, Prioridade prioridade,
        String cor, Boolean isRecorrente, String frequenciaRecorrencia
    ) {
        return new EventoCalendarioDto(
            id, TipoEvento.EVENTO_RECORRENTE, titulo, descricao, dataInicio,
            dataFim, diaInteiro, prioridade, cor, null, null,
            null, null, null,
            isRecorrente, frequenciaRecorrencia
        );
    }
}

