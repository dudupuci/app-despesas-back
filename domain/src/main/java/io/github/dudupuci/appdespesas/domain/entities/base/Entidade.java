package io.github.dudupuci.appdespesas.domain.entities.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Entidade implements Serializable {
    protected Long id;
    protected Date dataCriacao = new Date();
    protected Date dataAtualizacao = new Date();
}
