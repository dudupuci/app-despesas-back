package io.github.dudupuci.appdespesas.models.enums;

import lombok.Getter;

/**
 * Enum que define a prioridade de um evento/compromisso
 */
@Getter
public enum PrioridadeEvento {
    NENHUMA("Nenhuma"),
    BAIXA("Baixa"),
    MEDIA("Média"),
    ALTA("Alta"),
    URGENTE("Urgente");

    private final String descricao;

    PrioridadeEvento(String descricao) {
        this.descricao = descricao;
    }

}

