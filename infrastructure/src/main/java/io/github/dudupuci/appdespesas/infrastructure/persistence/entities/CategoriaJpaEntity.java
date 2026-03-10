package io.github.dudupuci.appdespesas.infrastructure.persistence.entities;

import io.github.dudupuci.appdespesas.domain.entities.Categoria;
import io.github.dudupuci.appdespesas.domain.enums.Status;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base.JpaEntidadeUuid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "categoria")
@Getter
@Setter
public class CategoriaJpaEntity extends JpaEntidadeUuid {

    private String nome;
    private String descricao;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tipo_movimentacao", nullable = false)
    private TipoMovimentacao tipoMovimentacao;

    @OneToMany(mappedBy = "categoria")
    private Set<MovimentacaoJpaEntity> movimentacoes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private UsuarioSistemaJpaEntity usuarioSistema;

    @ManyToOne
    @JoinColumn(name = "cor_id")
    private CorJpaEntity cor;

    public CategoriaJpaEntity() {
        super();
    }

    /**
     * Converte a entidade JPA para entidade de domínio
     */
    public Categoria toEntity() {
        Categoria categoria = new Categoria();
        super.toDomain(categoria);
        categoria.setNome(this.nome);
        categoria.setDescricao(this.descricao);
        categoria.setStatus(this.status);
        categoria.setTipoMovimentacao(this.tipoMovimentacao);

        if (this.cor != null) {
            categoria.setCor(this.cor.toEntity());
        }

        if (this.usuarioSistema != null) {
            categoria.setUsuarioSistema(this.usuarioSistema.toEntity());
        }

        if (this.movimentacoes != null && !this.movimentacoes.isEmpty()) {
            categoria.setMovimentacoes(
                this.movimentacoes.stream()
                    .map(MovimentacaoJpaEntity::toEntity)
                    .collect(Collectors.toSet())
            );
        }

        return categoria;
    }

    /**
     * Cria uma entidade JPA a partir de uma entidade de domínio (sem relacionamentos complexos)
     */
    public static CategoriaJpaEntity fromEntity(Categoria categoria) {
        if (categoria == null) return null;

        CategoriaJpaEntity categoriaJpaEntity = new CategoriaJpaEntity();
        categoriaJpaEntity.fromDomain(categoria);
        categoriaJpaEntity.setNome(categoria.getNome());
        categoriaJpaEntity.setDescricao(categoria.getDescricao());
        categoriaJpaEntity.setStatus(categoria.getStatus());
        categoriaJpaEntity.setTipoMovimentacao(categoria.getTipoMovimentacao());
        return categoriaJpaEntity;
    }

    /**
     * Atualiza a entidade JPA com dados da entidade de domínio
     */
    public void updateFromEntity(Categoria categoria) {
        this.nome = categoria.getNome();
        this.descricao = categoria.getDescricao();
        this.status = categoria.getStatus();
        this.tipoMovimentacao = categoria.getTipoMovimentacao();
        this.dataAtualizacao = categoria.getDataAtualizacao();
    }
}

