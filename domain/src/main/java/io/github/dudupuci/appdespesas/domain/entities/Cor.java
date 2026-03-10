package io.github.dudupuci.appdespesas.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeUuid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cor extends EntidadeUuid {

    private String nome;
    private String codigoHexadecimal;

    //@JsonIgnore
    private UsuarioSistema usuarioSistema;

    private Boolean isVisivel = true;


    public Cor(String nome, String codigoHexadecimal) {
        super();
        this.nome = nome;
        this.codigoHexadecimal = codigoHexadecimal;
    }

}
