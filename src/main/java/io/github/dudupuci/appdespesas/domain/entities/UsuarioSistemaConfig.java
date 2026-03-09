package io.github.dudupuci.appdespesas.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.dudupuci.appdespesas.domain.converters.CanalNotificacaoConverter;
import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeUuid;
import io.github.dudupuci.appdespesas.domain.enums.CanalNotificacao;
import io.github.dudupuci.appdespesas.domain.enums.Idioma;
import io.github.dudupuci.appdespesas.domain.enums.TomRespostaIA;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Table(name = "usuario_config")
@Entity
@Getter
@Setter
public class UsuarioSistemaConfig extends EntidadeUuid {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    @JsonIgnore
    private UsuarioSistema usuarioSistema;

    @Convert(converter = CanalNotificacaoConverter.class)
    private Set<CanalNotificacao> canaisNotificacao;

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

    public UsuarioSistemaConfig() {}

    public UsuarioSistemaConfig(UsuarioSistema usuarioSistema) {
        this.usuarioSistema = usuarioSistema;
        this.canaisNotificacao = new HashSet<>();
        this.tomRespostaIA = null;
        this.idiomaPreferidoIA = null;
        this.instrucoesIA = null;
        this.isIAAtiva = Boolean.FALSE;
    }
}
