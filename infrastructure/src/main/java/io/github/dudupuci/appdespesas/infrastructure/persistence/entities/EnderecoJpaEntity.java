package io.github.dudupuci.appdespesas.infrastructure.persistence.entities;

import io.github.dudupuci.appdespesas.domain.entities.Endereco;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base.JpaEntidadeUuid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "endereco")
@Getter
@Setter
public class EnderecoJpaEntity extends JpaEntidadeUuid {

    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;

    @Column(length = 8)
    private String cep;

    public EnderecoJpaEntity() {
        super();
    }

    /**
     * Converte a entidade JPA para entidade de domínio
     */
    public Endereco toEntity() {
        Endereco endereco = new Endereco();
        super.toDomain(endereco);
        endereco.setLogradouro(this.logradouro);
        endereco.setNumero(this.numero);
        endereco.setComplemento(this.complemento);
        endereco.setBairro(this.bairro);
        endereco.setCep(this.cep);
        return endereco;
    }

    /**
     * Cria uma entidade JPA a partir de uma entidade de domínio
     */
    public static EnderecoJpaEntity fromEntity(Endereco endereco) {
        if (endereco == null) return null;

        EnderecoJpaEntity enderecoJpaEntity = new EnderecoJpaEntity();
        enderecoJpaEntity.fromDomain(endereco);
        enderecoJpaEntity.setLogradouro(endereco.getLogradouro());
        enderecoJpaEntity.setNumero(endereco.getNumero());
        enderecoJpaEntity.setComplemento(endereco.getComplemento());
        enderecoJpaEntity.setBairro(endereco.getBairro());
        enderecoJpaEntity.setCep(endereco.getCep());
        return enderecoJpaEntity;
    }

    /**
     * Atualiza a entidade JPA com dados da entidade de domínio
     */
    public void updateFromEntity(Endereco endereco) {
        this.logradouro = endereco.getLogradouro();
        this.numero = endereco.getNumero();
        this.complemento = endereco.getComplemento();
        this.bairro = endereco.getBairro();
        this.cep = endereco.getCep();
        this.dataAtualizacao = endereco.getDataAtualizacao();
    }
}

