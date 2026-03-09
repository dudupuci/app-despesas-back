package io.github.dudupuci.appdespesas.domain.entities.base;

import io.github.dudupuci.appdespesas.domain.enums.CanalNotificacao;
import io.github.dudupuci.appdespesas.domain.enums.TipoNotificacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@ToString
@Getter
@Setter
@NoArgsConstructor
public abstract class Notificacao extends EntidadeImutavelUuid {
    private String titulo;
    private String mensagem;
    private UUID usuarioId;
    private TipoNotificacao tipoNotificacao;
    private CanalNotificacao canalNotificacao;


    public Notificacao(String titulo, String mensagem, UUID usuarioId, TipoNotificacao tipoNotificacao, CanalNotificacao canalNotificacao) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.usuarioId = usuarioId;
        this.tipoNotificacao = tipoNotificacao;
        this.canalNotificacao = canalNotificacao;
    }
}