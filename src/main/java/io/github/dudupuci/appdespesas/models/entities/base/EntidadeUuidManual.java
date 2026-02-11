package io.github.dudupuci.appdespesas.models.entities.base;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Classe base para entidades que precisam de UUID MANUAL (não gerado automaticamente)
 * Usada para entidades do sistema que precisam ter IDs fixos e conhecidos.
 *
 * Exemplo: Administrador do sistema, dados padrão, etc.
 */
@ToString
@MappedSuperclass
@Getter
@Setter
public abstract class EntidadeUuidManual implements Serializable {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    protected UUID id;

    @Column(name = "data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dataCriacao = new Date();

    @Column(name = "data_atualizacao")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dataAtualizacao = new Date();

    protected EntidadeUuidManual() {}

    protected EntidadeUuidManual(UUID id, Date dataCriacao, Date dataAtualizacao) {
        this.id = id;
        this.dataCriacao = dataCriacao;
        this.dataAtualizacao = dataAtualizacao;
    }
}

