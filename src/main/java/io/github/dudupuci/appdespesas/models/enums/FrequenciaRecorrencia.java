package io.github.dudupuci.appdespesas.models.enums;

import lombok.Getter;

/**
 * Enum que define a frequência de recorrência de um evento
 */
@Getter
public enum FrequenciaRecorrencia {
    DIARIA("Diária"),
    SEMANAL("Semanal"),
    QUINZENAL("Quinzenal"),
    MENSAL("Mensal"),
    BIMESTRAL("Bimestral"),
    TRIMESTRAL("Trimestral"),
    SEMESTRAL("Semestral"),
    ANUAL("Anual");

    private final String descricao;

    FrequenciaRecorrencia(String descricao) {
        this.descricao = descricao;
    }

}
