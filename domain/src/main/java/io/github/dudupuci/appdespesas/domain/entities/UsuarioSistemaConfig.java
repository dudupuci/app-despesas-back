package io.github.dudupuci.appdespesas.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeUuid;
import io.github.dudupuci.appdespesas.domain.enums.CanalNotificacao;
import io.github.dudupuci.appdespesas.domain.enums.Idioma;
import io.github.dudupuci.appdespesas.domain.enums.TomRespostaIA;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioSistemaConfig extends EntidadeUuid {

    //@JsonIgnore
    private UsuarioSistema usuarioSistema;

    private Set<CanalNotificacao> canaisNotificacao = new HashSet<>();
    private TomRespostaIA tomRespostaIA;
    private Idioma idiomaPreferidoIA;
    private String instrucoesIA;
    private Boolean isIAAtiva;


    public UsuarioSistemaConfig(UsuarioSistema usuarioSistema) {
        this.usuarioSistema = usuarioSistema;
        this.canaisNotificacao = new HashSet<>();
        this.tomRespostaIA = null;
        this.idiomaPreferidoIA = null;
        this.instrucoesIA = null;
        this.isIAAtiva = Boolean.FALSE;
    }
}
