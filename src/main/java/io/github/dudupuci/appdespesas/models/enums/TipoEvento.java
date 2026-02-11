package io.github.dudupuci.appdespesas.models.enums;

import lombok.Getter;

/**
 * Enum que define os tipos de eventos que podem aparecer no calendário
 */
@Getter
public enum TipoEvento {
    COMPROMISSO("Compromisso"),
    MOVIMENTACAO_PREVISTA("Movimentação Prevista"),
    EVENTO_RECORRENTE("Evento Recorrente"),
    LEMBRETE("Lembrete");

    private final String descricao;

    TipoEvento(String descricao) {
        this.descricao = descricao;
    }

}
