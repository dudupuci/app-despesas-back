package io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base;

import io.github.dudupuci.appdespesas.domain.entities.base.Pessoa;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class JpaPessoa extends JpaEntidadeUuid {

    protected String nome;
    protected String sobrenome;

    /**
     * Copia os dados da entidade de domínio para a entidade JPA
     */
    protected void fromDomainPessoa(Pessoa domain) {
        super.fromDomain(domain);
        this.nome = domain.getNome();
        this.sobrenome = domain.getSobrenome();
    }

    /**
     * Copia os dados da entidade JPA para a entidade de domínio
     */
    protected void toDomainPessoa(Pessoa domain) {
        super.toDomain(domain);
        domain.setNome(this.nome);
        domain.setSobrenome(this.sobrenome);
    }

    public String getNomeCompleto() {
        return nome + " " + sobrenome;
    }
}

