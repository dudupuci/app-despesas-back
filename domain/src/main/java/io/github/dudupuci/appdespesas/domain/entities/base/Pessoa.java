package io.github.dudupuci.appdespesas.domain.entities.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Pessoa extends EntidadeUuid {
    protected String nome;
    protected String sobrenome;

    protected String getNomeCompleto() {
        return nome + " " + sobrenome;
    }
}
