package io.github.dudupuci.appdespesas.domain.entities.base;

import io.github.dudupuci.appdespesas.domain.enums.CanalNotificacao;
import io.github.dudupuci.appdespesas.domain.enums.TipoNotificacao;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.UUID;

@ToString
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "canalNotificacao")
@Getter
@Setter
public abstract class Notificacao extends EntidadeImutavelUuid {
    private String titulo;
    private String mensagem;
    private UUID usuarioId;
    private TipoNotificacao tipoNotificacao;
    private CanalNotificacao canalNotificacao;

    public Notificacao() {

    }

    public Notificacao(String titulo, String mensagem, UUID usuarioId, TipoNotificacao tipoNotificacao, CanalNotificacao canalNotificacao) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.usuarioId = usuarioId;
        this.tipoNotificacao = tipoNotificacao;
        this.canalNotificacao = canalNotificacao;
    }
}