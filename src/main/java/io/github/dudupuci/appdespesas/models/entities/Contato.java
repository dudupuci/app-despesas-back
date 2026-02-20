package io.github.dudupuci.appdespesas.models.entities;

import io.github.dudupuci.appdespesas.models.entities.base.EntidadeUuid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contatos")
@Getter
@Setter
public class Contato extends EntidadeUuid {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, length = 11)
    private String celular;

    @Column(name = "telefone_fixo", unique = true, length = 10)
    private String telefoneFixo;
}
