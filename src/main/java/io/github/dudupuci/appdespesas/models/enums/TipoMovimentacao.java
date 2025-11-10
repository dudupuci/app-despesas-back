package io.github.dudupuci.appdespesas.models.enums;

import lombok.Getter;

public enum TipoMovimentacao {
    DESPESA("Despesa"),
    ENTRADA("Entrada");

    @Getter
    private final String descricao;

    TipoMovimentacao(String descricao) {
        this.descricao = descricao;
    }

    TipoMovimentacao buscarPorDescricao(String descricao) {
        for (TipoMovimentacao tipo : TipoMovimentacao.values()) {
            if (tipo.getDescricao().equalsIgnoreCase(descricao)) {
                return tipo;
            }
        }
        return null;
    }
}
