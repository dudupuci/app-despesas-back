package io.github.dudupuci.appdespesas.infrastructure.persistence.entities;

import io.github.dudupuci.appdespesas.domain.entities.Contato;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base.JpaEntidadeUuid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contato")
@Getter
@Setter
public class ContatoJpaEntity extends JpaEntidadeUuid {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, length = 11)
    private String celular;

    @Column(name = "telefone_fixo", unique = true, length = 10)
    private String telefoneFixo;

    public ContatoJpaEntity() {
        super();
    }

    /**
     * Converte a entidade JPA para entidade de domínio
     */
    public Contato toEntity() {
        Contato contato = new Contato();
        super.toDomain(contato);
        contato.setEmail(this.email);
        contato.setCelular(this.celular);
        contato.setTelefoneFixo(this.telefoneFixo);
        return contato;
    }

    /**
     * Cria uma entidade JPA a partir de uma entidade de domínio
     */
    public static ContatoJpaEntity fromEntity(Contato contato) {
        if (contato == null) return null;

        ContatoJpaEntity contatoJpaEntity = new ContatoJpaEntity();
        contatoJpaEntity.fromDomain(contato);
        contatoJpaEntity.setEmail(contato.getEmail());
        contatoJpaEntity.setCelular(contato.getCelular());
        contatoJpaEntity.setTelefoneFixo(contato.getTelefoneFixo());
        return contatoJpaEntity;
    }

    /**
     * Atualiza a entidade JPA com dados da entidade de domínio
     */
    public void updateFromEntity(Contato contato) {
        this.email = contato.getEmail();
        this.celular = contato.getCelular();
        this.telefoneFixo = contato.getTelefoneFixo();
        this.dataAtualizacao = contato.getDataAtualizacao();
    }
}

