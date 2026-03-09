package io.github.dudupuci.appdespesas.domain.enums;

import lombok.Getter;

/**
 * Enum que define a prioridade de um evento/compromisso
 */
@Getter
public enum Prioridade {
    NENHUMA("Nenhuma"),
    BAIXA("Baixa"),
    MEDIA("Média"),
    ALTA("Alta"),
    URGENTE("Urgente");

    private final String descricao;

    Prioridade(String descricao) {
        this.descricao = descricao;
    }

}

