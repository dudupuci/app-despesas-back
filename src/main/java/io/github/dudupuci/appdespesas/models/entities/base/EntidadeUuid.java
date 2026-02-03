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
public abstract class EntidadeUuid implements Serializable {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    protected UUID id;

    @Column(name = "data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dataCriacao = new Date();

    @Column(name = "data_atualizacao")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dataAtualizacao = new Date();

    protected EntidadeUuid(){}

    protected EntidadeUuid(UUID id, Date dataCriacao, Date dataAtualizacao) {
        this.id = id;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }
}
