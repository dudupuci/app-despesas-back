package io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base;

import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeImutavelUuid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class JpaEntidadeImutavelUuid extends EntidadeImutavelUuid {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    protected UUID id;

    @Column(name = "data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dataCriacao = new Date();

    protected JpaEntidadeImutavelUuid() {
        super();
    }

    /**
     * Copia os dados da entidade de domínio para a entidade JPA
     */
    protected void fromDomain(EntidadeImutavelUuid domain) {
        this.id = domain.getId();
        this.dataCriacao = domain.getDataCriacao();
    }

    /**
     * Copia os dados da entidade JPA para a entidade de domínio
     */
    protected void toDomain(EntidadeImutavelUuid domain) {
        domain.setId(this.id);
        domain.setDataCriacao(this.dataCriacao);
    }
}

