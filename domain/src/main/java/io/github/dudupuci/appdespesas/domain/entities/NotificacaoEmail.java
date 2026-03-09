package io.github.dudupuci.appdespesas.domain.entities;

import io.github.dudupuci.appdespesas.domain.entities.base.Notificacao;
import io.github.dudupuci.appdespesas.domain.enums.CanalNotificacao;
import io.github.dudupuci.appdespesas.domain.enums.TipoNotificacao;

import java.util.UUID;

public class NotificacaoEmail extends Notificacao {

    public NotificacaoEmail(String titulo, String mensagem, UUID usuarioId, TipoNotificacao tipoNotificacao, CanalNotificacao canalNotificacao) {
        super(titulo, mensagem, usuarioId, tipoNotificacao, canalNotificacao);
    }

    public NotificacaoEmail() {
        super();
    }
}
