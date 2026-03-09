package io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base;

import io.github.dudupuci.appdespesas.domain.entities.base.Entidade;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public abstract class JpaEntidade extends Entidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    protected Long id;

    @Column(name = "data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dataCriacao = new Date();

    @Column(name = "data_atualizacao")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dataAtualizacao = new Date();

    protected JpaEntidade() {
        super();
    }

    /**
     * Copia os dados da entidade de domínio para a entidade JPA
     */
    protected void fromDomain(Entidade domain) {
        this.id = domain.getId();
        this.dataCriacao = domain.getDataCriacao();
        this.dataAtualizacao = domain.getDataAtualizacao();
    }

    /**
     * Copia os dados da entidade JPA para a entidade de domínio
     */
    protected void toDomain(Entidade domain) {
        domain.setId(this.id);
        domain.setDataCriacao(this.dataCriacao);
        domain.setDataAtualizacao(this.dataAtualizacao);
    }
}

