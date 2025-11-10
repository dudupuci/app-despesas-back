package io.github.dudupuci.appdespesas.models.entities.base;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Pessoa extends Entidade{
    protected String nome;
    protected String sobrenome;
}
