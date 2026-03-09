package io.github.dudupuci.appdespesas.domain.enums;

public enum CanalNotificacao {
    EMAIL("Email"),
    SMS("Sms"),
    PUSH_NOTIFICATION("Push Notification"),
    WHATSAPP("WhatsApp");

    private final String descricao;

    CanalNotificacao(String descricao) {
        this.descricao = descricao;
    }
}
