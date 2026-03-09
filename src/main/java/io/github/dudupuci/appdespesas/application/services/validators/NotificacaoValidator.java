package io.github.dudupuci.appdespesas.application.services.validators;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

public class NotificacaoValidator {

    @Getter
    @Setter
    private Map<String, String> notificacoesEnviadasAoUsuario;

    /**
     * Registra que uma notificação foi enviada ao usuário.
     * @param tipoNotificacao Tipo da notificação (ex: "EMAIL_CRIACAO", "SMS_VENCIMENTO", "PUSH_PAGAMENTO_CONFIRMADO")
     * @param idNotificacao Identificador da notificação enviada
     */
    public void registrarNotificacaoEnviada(String tipoNotificacao, String idNotificacao) {
        if (this.notificacoesEnviadasAoUsuario == null) {
            this.notificacoesEnviadasAoUsuario = new HashMap<>();
        }
        this.notificacoesEnviadasAoUsuario.put(tipoNotificacao, idNotificacao);
    }

    /**
     * Verifica se uma notificação específica já foi enviada.
     * @param tipoNotificacao Tipo da notificação a verificar
     * @return true se já foi enviada, false caso contrário
     */
    public boolean notificacaoJaEnviada(String tipoNotificacao) {
        return this.notificacoesEnviadasAoUsuario != null
                && this.notificacoesEnviadasAoUsuario.containsKey(tipoNotificacao);
    }


    /**
     * Obtém todas as notificações enviadas para esta cobrança.
     * @return Map com tipo da notificação → id da notificação enviada
     */
    public Map<String, String> obterHistoricoNotificacoes() {
        return this.notificacoesEnviadasAoUsuario != null
                ? new HashMap<>(this.notificacoesEnviadasAoUsuario)
                : new HashMap<>();
    }
}
