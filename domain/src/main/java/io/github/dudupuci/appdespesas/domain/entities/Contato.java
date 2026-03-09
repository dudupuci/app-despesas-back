package io.github.dudupuci.appdespesas.domain.entities;

import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeUuid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contato extends EntidadeUuid {

    private String email;
    private String celular;
    private String telefoneFixo;
}
