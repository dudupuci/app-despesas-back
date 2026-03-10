package io.github.dudupuci.appdespesas.infrastructure.persistence.entities;

import io.github.dudupuci.appdespesas.domain.entities.NotificacaoEmail;
import io.github.dudupuci.appdespesas.domain.enums.CanalNotificacao;
import io.github.dudupuci.appdespesas.domain.enums.TipoNotificacao;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base.JpaEntidadeImutavelUuid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "notificacao_email")
@DiscriminatorValue("email")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "canal_notificacao")
@Getter
@Setter
public class NotificacaoEmailJpaEntity extends JpaEntidadeImutavelUuid {

    private String titulo;
    private String mensagem;

    @Column(name = "usuario_id")
    private UUID usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_notificacao")
    private TipoNotificacao tipoNotificacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal_notificacao", insertable = false, updatable = false)
    private CanalNotificacao canalNotificacao;

    public NotificacaoEmailJpaEntity() {
        super();
    }

    public NotificacaoEmailJpaEntity(String titulo, String mensagem, UUID usuarioId, TipoNotificacao tipoNotificacao, CanalNotificacao canalNotificacao) {
        super();
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.usuarioId = usuarioId;
        this.tipoNotificacao = tipoNotificacao;
        this.canalNotificacao = canalNotificacao;
    }

    /**
     * Converte a entidade JPA para entidade de domínio
     */
    public NotificacaoEmail toEntity() {
        NotificacaoEmail notificacao = new NotificacaoEmail();
        super.toDomain(notificacao);
        notificacao.setTitulo(this.titulo);
        notificacao.setMensagem(this.mensagem);
        notificacao.setUsuarioId(this.usuarioId);
        notificacao.setTipoNotificacao(this.tipoNotificacao);
        notificacao.setCanalNotificacao(this.canalNotificacao);
        return notificacao;
    }

    /**
     * Cria uma entidade JPA a partir de uma entidade de domínio
     */
    public static NotificacaoEmailJpaEntity fromEntity(NotificacaoEmail notificacao) {
        if (notificacao == null) return null;

        NotificacaoEmailJpaEntity jpaNotificacao = new NotificacaoEmailJpaEntity();
        jpaNotificacao.fromDomain(notificacao);
        jpaNotificacao.setTitulo(notificacao.getTitulo());
        jpaNotificacao.setMensagem(notificacao.getMensagem());
        jpaNotificacao.setUsuarioId(notificacao.getUsuarioId());
        jpaNotificacao.setTipoNotificacao(notificacao.getTipoNotificacao());
        jpaNotificacao.setCanalNotificacao(notificacao.getCanalNotificacao());
        return jpaNotificacao;
    }
}

