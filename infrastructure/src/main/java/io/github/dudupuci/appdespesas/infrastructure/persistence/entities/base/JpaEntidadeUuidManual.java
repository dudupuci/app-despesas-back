package io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base;

import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeUuidManual;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class JpaEntidadeUuidManual extends EntidadeUuidManual {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    protected UUID id;

    @Column(name = "data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dataCriacao = new Date();

    @Column(name = "data_atualizacao")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dataAtualizacao = new Date();

    protected JpaEntidadeUuidManual() {
        super();
    }

    /**
     * Copia os dados da entidade de domínio para a entidade JPA
     */
    protected void fromDomain(EntidadeUuidManual domain) {
        this.id = domain.getId();
        this.dataCriacao = domain.getDataCriacao();
        this.dataAtualizacao = domain.getDataAtualizacao();
    }

    /**
     * Copia os dados da entidade JPA para a entidade de domínio
     */
    protected void toDomain(EntidadeUuidManual domain) {
        domain.setId(this.id);
        domain.setDataCriacao(this.dataCriacao);
        domain.setDataAtualizacao(this.dataAtualizacao);
    }
}

