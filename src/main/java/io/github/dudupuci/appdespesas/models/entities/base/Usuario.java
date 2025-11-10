package io.github.dudupuci.appdespesas.models.entities.base;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Usuario extends Pessoa {
    protected String login;
    protected String email;
    protected String senha;
}
