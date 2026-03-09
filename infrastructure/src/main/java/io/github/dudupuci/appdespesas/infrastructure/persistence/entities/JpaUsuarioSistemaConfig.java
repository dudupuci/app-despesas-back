package io.github.dudupuci.appdespesas.infrastructure.persistence.entities;

import io.github.dudupuci.appdespesas.domain.converters.CanalNotificacaoConverter;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistemaConfig;
import io.github.dudupuci.appdespesas.domain.enums.CanalNotificacao;
import io.github.dudupuci.appdespesas.domain.enums.Idioma;
import io.github.dudupuci.appdespesas.domain.enums.TomRespostaIA;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base.JpaEntidadeUuid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuario_config")
@Getter
@Setter
public class JpaUsuarioSistemaConfig extends JpaEntidadeUuid {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private JpaUsuarioSistema usuarioSistema;

    @Convert(converter = CanalNotificacaoConverter.class)
    @Column(name = "canais_notificacao")
    private Set<CanalNotificacao> canaisNotificacao = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "tom_resposta_ia")
    private TomRespostaIA tomRespostaIA;

    @Column(name = "idioma_preferido_ia")
    @Enumerated(EnumType.STRING)
    private Idioma idiomaPreferidoIA;

    @Column(name = "instrucoes_ia")
    private String instrucoesIA;

    @Column(name = "ia_ativa")
    private Boolean isIAAtiva;

    public JpaUsuarioSistemaConfig() {
        super();
    }

    /**
     * Converte a entidade JPA para entidade de domínio
     */
    public UsuarioSistemaConfig toEntity() {
        UsuarioSistemaConfig config = new UsuarioSistemaConfig();
        super.toDomain(config);
        config.setCanaisNotificacao(this.canaisNotificacao);
        config.setTomRespostaIA(this.tomRespostaIA);
        config.setIdiomaPreferidoIA(this.idiomaPreferidoIA);
        config.setInstrucoesIA(this.instrucoesIA);
        config.setIsIAAtiva(this.isIAAtiva);

        if (this.usuarioSistema != null) {
            config.setUsuarioSistema(this.usuarioSistema.toEntity());
        }

        return config;
    }

    /**
     * Cria uma entidade JPA a partir de uma entidade de domínio (sem relacionamentos)
     */
    public static JpaUsuarioSistemaConfig fromEntity(UsuarioSistemaConfig config) {
        if (config == null) return null;

        JpaUsuarioSistemaConfig jpaConfig = new JpaUsuarioSistemaConfig();
        jpaConfig.fromDomain(config);
        jpaConfig.setCanaisNotificacao(config.getCanaisNotificacao());
        jpaConfig.setTomRespostaIA(config.getTomRespostaIA());
        jpaConfig.setIdiomaPreferidoIA(config.getIdiomaPreferidoIA());
        jpaConfig.setInstrucoesIA(config.getInstrucoesIA());
        jpaConfig.setIsIAAtiva(config.getIsIAAtiva());
        return jpaConfig;
    }

    /**
     * Atualiza a entidade JPA com dados da entidade de domínio
     */
    public void updateFromEntity(UsuarioSistemaConfig config) {
        this.canaisNotificacao = config.getCanaisNotificacao();
        this.tomRespostaIA = config.getTomRespostaIA();
        this.idiomaPreferidoIA = config.getIdiomaPreferidoIA();
        this.instrucoesIA = config.getInstrucoesIA();
        this.isIAAtiva = config.getIsIAAtiva();
        this.dataAtualizacao = config.getDataAtualizacao();
    }
}

