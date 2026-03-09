package io.github.dudupuci.appdespesas.domain.entities;

import io.github.dudupuci.appdespesas.domain.entities.base.Entidade;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "role")
@Getter
@Setter
public class Role extends Entidade {
    private String nome;
    private String descricao;
    private Integer poder;

    public Role() {
        super();
    }

    public Role(String nome, String descricao, Integer poder) {
        super();
        this.nome = nome;
        this.descricao = descricao;
        this.poder = poder;
    }
}
