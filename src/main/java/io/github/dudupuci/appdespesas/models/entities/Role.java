package io.github.dudupuci.appdespesas.models.entities;

import io.github.dudupuci.appdespesas.models.entities.base.Entidade;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "roles")
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
