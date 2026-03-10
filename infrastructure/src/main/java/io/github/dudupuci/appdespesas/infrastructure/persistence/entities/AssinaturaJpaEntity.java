package io.github.dudupuci.appdespesas.infrastructure.persistence.entities;

import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base.JpaEntidade;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "assinatura")
@Getter
@Setter
public class AssinaturaJpaEntity extends JpaEntidade {

    @Column(name = "nome_plano")
    private String nomePlano;

    private BigDecimal valor;
    private String descricao;

    @ElementCollection
    @CollectionTable(name = "assinatura_beneficios", joinColumns = @JoinColumn(name = "assinatura_id"))
    @Column(name = "beneficio")
    private List<String> beneficios;

    public AssinaturaJpaEntity() {
        super();
    }

    /**
     * Converte a entidade JPA para entidade de domínio
     */
    public Assinatura toEntity() {
        Assinatura assinatura = new Assinatura();
        super.toDomain(assinatura);
        assinatura.setNomePlano(this.nomePlano);
        assinatura.setValor(this.valor);
        assinatura.setDescricao(this.descricao);
        assinatura.setBeneficios(this.beneficios);
        return assinatura;
    }

    /**
     * Cria uma entidade JPA a partir de uma entidade de domínio
     */
    public static AssinaturaJpaEntity fromEntity(Assinatura assinatura) {
        if (assinatura == null) return null;

        AssinaturaJpaEntity assinaturaJpaEntity = new AssinaturaJpaEntity();
        assinaturaJpaEntity.fromDomain(assinatura);
        assinaturaJpaEntity.setNomePlano(assinatura.getNomePlano());
        assinaturaJpaEntity.setValor(assinatura.getValor());
        assinaturaJpaEntity.setDescricao(assinatura.getDescricao());
        assinaturaJpaEntity.setBeneficios(assinatura.getBeneficios());
        return assinaturaJpaEntity;
    }

    /**
     * Atualiza a entidade JPA com dados da entidade de domínio
     */
    public void updateFromEntity(Assinatura assinatura) {
        this.nomePlano = assinatura.getNomePlano();
        this.valor = assinatura.getValor();
        this.descricao = assinatura.getDescricao();
        this.beneficios = assinatura.getBeneficios();
        this.dataAtualizacao = assinatura.getDataAtualizacao();
    }
}

