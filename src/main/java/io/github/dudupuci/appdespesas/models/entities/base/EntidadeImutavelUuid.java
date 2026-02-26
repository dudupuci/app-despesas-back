package io.github.dudupuci.appdespesas.models.entities.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;


@ToString
@MappedSuperclass
@Getter
@Setter
public abstract class EntidadeImutavelUuid implements Serializable {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    protected UUID id;

    @Column(name = "data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dataCriacao = new Date();

    protected EntidadeImutavelUuid(){}

    protected EntidadeImutavelUuid(UUID id, Date dataCriacao) {
        this.id = id;
        this.dataCriacao = dataCriacao;
    }
}
