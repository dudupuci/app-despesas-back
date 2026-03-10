package io.github.dudupuci.appdespesas.infrastructure.persistence.entities;

import io.github.dudupuci.appdespesas.domain.entities.Cor;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base.JpaEntidadeUuid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cor")
@Getter
@Setter
public class CorJpaEntity extends JpaEntidadeUuid {

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(name = "codigo_hexadecimal", nullable = false, length = 7)
    private String codigoHexadecimal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuarioSistemaJpaEntity usuarioSistema;

    @Column(name = "is_visivel")
    private Boolean isVisivel = true;

    public CorJpaEntity() {
        super();
    }

    /**
     * Converte a entidade JPA para entidade de domínio
     */
    public Cor toEntity() {
        Cor cor = new Cor();
        super.toDomain(cor);
        cor.setNome(this.nome);
        cor.setCodigoHexadecimal(this.codigoHexadecimal);
        cor.setIsVisivel(this.isVisivel);
        if (this.usuarioSistema != null) {
            cor.setUsuarioSistema(this.usuarioSistema.toEntity());
        }
        return cor;
    }

    /**
     * Cria uma entidade JPA a partir de uma entidade de domínio (sem relacionamentos)
     */
    public static CorJpaEntity fromEntity(Cor cor) {
        if (cor == null) return null;

        CorJpaEntity corJpaEntity = new CorJpaEntity();
        corJpaEntity.fromDomain(cor);
        corJpaEntity.setNome(cor.getNome());
        corJpaEntity.setCodigoHexadecimal(cor.getCodigoHexadecimal());
        corJpaEntity.setIsVisivel(cor.getIsVisivel());
        return corJpaEntity;
    }

    /**
     * Atualiza a entidade JPA com dados da entidade de domínio
     */
    public void updateFromEntity(Cor cor) {
        this.nome = cor.getNome();
        this.codigoHexadecimal = cor.getCodigoHexadecimal();
        this.isVisivel = cor.getIsVisivel();
        this.dataAtualizacao = cor.getDataAtualizacao();
    }
}

