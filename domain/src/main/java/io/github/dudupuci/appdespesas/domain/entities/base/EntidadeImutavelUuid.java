package io.github.dudupuci.appdespesas.domain.entities.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class EntidadeImutavelUuid implements Serializable {

    protected UUID id;
    protected Date dataCriacao = new Date();

}
