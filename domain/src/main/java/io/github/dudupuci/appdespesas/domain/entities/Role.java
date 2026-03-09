package io.github.dudupuci.appdespesas.domain.entities;

import io.github.dudupuci.appdespesas.domain.entities.base.Entidade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role extends Entidade {
    private String nome;
    private String descricao;
    private Integer poder;


    public Role(String nome, String descricao, Integer poder) {
        super();
        this.nome = nome;
        this.descricao = descricao;
        this.poder = poder;
    }
}
