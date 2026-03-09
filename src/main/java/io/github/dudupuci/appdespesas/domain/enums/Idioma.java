package io.github.dudupuci.appdespesas.domain.enums;

public enum Idioma {
    PORTUGUES_PT_BR("Português (Brasil)"),
    INGLES_EN_US("Inglês (Estados Unidos)");

    private final String descricao;

    Idioma(String descricao) {
        this.descricao = descricao;
    }
}
