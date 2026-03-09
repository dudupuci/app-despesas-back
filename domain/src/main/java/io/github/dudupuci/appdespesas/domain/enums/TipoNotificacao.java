package io.github.dudupuci.appdespesas.domain.enums;

import lombok.Getter;

/**
 * Tipos de notificações relacionadas ao ciclo de vida de uma cobrança.
 * Útil para rastrear quais comunicações foram enviadas ao usuário.
 */
@Getter
public enum TipoNotificacao {

    // Notificações de Criação
    EMAIL_COBRANCA_CRIADA("E-mail de cobrança criada", "Enviado quando a cobrança é criada"),
    SMS_COBRANCA_CRIADA("SMS de cobrança criada", "Enviado quando a cobrança é criada"),
    PUSH_COBRANCA_CRIADA("Notificação push de cobrança criada", "Enviado quando a cobrança é criada"),
    WHATSAPP_COBRANCA_CRIADA("WhatsApp de cobrança criada", "Enviado quando a cobrança é criada"),

    // Notificações de Vencimento
    EMAIL_VENCIMENTO_PROXIMO("E-mail de vencimento próximo", "Enviado X dias antes do vencimento"),
    SMS_VENCIMENTO_PROXIMO("SMS de vencimento próximo", "Enviado X dias antes do vencimento"),
    PUSH_VENCIMENTO_PROXIMO("Push de vencimento próximo", "Enviado X dias antes do vencimento"),
    WHATSAPP_VENCIMENTO_PROXIMO("WhatsApp de vencimento próximo", "Enviado X dias antes do vencimento"),

    EMAIL_DIA_VENCIMENTO("E-mail no dia do vencimento", "Enviado no dia do vencimento"),
    SMS_DIA_VENCIMENTO("SMS no dia do vencimento", "Enviado no dia do vencimento"),
    PUSH_DIA_VENCIMENTO("Push no dia do vencimento", "Enviado no dia do vencimento"),
    WHATSAPP_DIA_VENCIMENTO("WhatsApp no dia do vencimento", "Enviado no dia do vencimento"),

    // Notificações de Atraso
    EMAIL_COBRANCA_VENCIDA("E-mail de cobrança vencida", "Enviado após o vencimento"),
    SMS_COBRANCA_VENCIDA("SMS de cobrança vencida", "Enviado após o vencimento"),
    PUSH_COBRANCA_VENCIDA("Push de cobrança vencida", "Enviado após o vencimento"),
    WHATSAPP_COBRANCA_VENCIDA("WhatsApp de cobrança vencida", "Enviado após o vencimento"),

    // Notificações de Pagamento
    EMAIL_PAGAMENTO_CONFIRMADO("E-mail de pagamento confirmado", "Enviado quando o pagamento é confirmado"),
    SMS_PAGAMENTO_CONFIRMADO("SMS de pagamento confirmado", "Enviado quando o pagamento é confirmado"),
    PUSH_PAGAMENTO_CONFIRMADO("Push de pagamento confirmado", "Enviado quando o pagamento é confirmado"),
    WHATSAPP_PAGAMENTO_CONFIRMADO("WhatsApp de pagamento confirmado", "Enviado quando o pagamento é confirmado"),

    EMAIL_PAGAMENTO_RECUSADO("E-mail de pagamento recusado", "Enviado quando o pagamento é recusado"),
    SMS_PAGAMENTO_RECUSADO("SMS de pagamento recusado", "Enviado quando o pagamento é recusado"),
    PUSH_PAGAMENTO_RECUSADO("Push de pagamento recusado", "Enviado quando o pagamento é recusado"),
    WHATSAPP_PAGAMENTO_RECUSADO("WhatsApp de pagamento recusado", "Enviado quando o pagamento é recusado"),

    // Notificações de Cancelamento/Reembolso
    EMAIL_COBRANCA_CANCELADA("E-mail de cobrança cancelada", "Enviado quando a cobrança é cancelada"),
    SMS_COBRANCA_CANCELADA("SMS de cobrança cancelada", "Enviado quando a cobrança é cancelada"),
    PUSH_COBRANCA_CANCELADA("Push de cobrança cancelada", "Enviado quando a cobrança é cancelada"),
    WHATSAPP_COBRANCA_CANCELADA("WhatsApp de cobrança cancelada", "Enviado quando a cobrança é cancelada"),

    EMAIL_REEMBOLSO_PROCESSADO("E-mail de reembolso processado", "Enviado quando o reembolso é processado"),
    SMS_REEMBOLSO_PROCESSADO("SMS de reembolso processado", "Enviado quando o reembolso é processado"),
    PUSH_REEMBOLSO_PROCESSADO("Push de reembolso processado", "Enviado quando o reembolso é processado"),
    WHATSAPP_REEMBOLSO_PROCESSADO("WhatsApp de reembolso processado", "Enviado quando o reembolso é processado"),

    // Notificações de Assinatura
    ASSINATURA_ATIVADA("Assinatura ativada", "Enviado quando a assinatura é ativada"),
    ASSINATURA_CANCELADA("Assinatura cancelada", "Enviado quando a assinatura é cancelada"),
    ASSINATURA_EXPIRADA("Assinatura expirada", "Enviado quando a assinatura expira"),
    ASSINATURA_RENOVADA("Assinatura renovada", "Enviado quando a assinatura é renovada");

    private final String descricao;
    private final String contexto;

    TipoNotificacao(String descricao, String contexto) {
        this.descricao = descricao;
        this.contexto = contexto;
    }

}

