package io.github.dudupuci.appdespesas.models.entities.base;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class Pessoa extends Entidade {
    protected String nome;
    protected String sobrenome;
}
