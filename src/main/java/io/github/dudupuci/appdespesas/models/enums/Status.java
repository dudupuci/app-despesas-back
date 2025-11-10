package io.github.dudupuci.appdespesas.models.enums;


import lombok.Getter;

@Getter
public enum Status {
    ATIVO("Ativo"),
    INATIVO("Inativo");

    private final String nome;

    Status(String nome) {
        this.nome = nome;
    }
}
