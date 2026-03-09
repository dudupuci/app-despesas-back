package io.github.dudupuci.appdespesas.domain.enums;


import lombok.Getter;

@Getter
public enum Status {
    ATIVO("Ativo"),
    INATIVO("Inativo"),
    PAGO("Pago"),
    CONFIRMADO("Confirmado"),
    AGUARDANDO_PAGAMENTO("Aguardando Pagamento"),
    CANCELADO("Cancelado"),
    FALHOU("Falhou");

    private final String nome;

    Status(String nome) {
        this.nome = nome;
    }
}
