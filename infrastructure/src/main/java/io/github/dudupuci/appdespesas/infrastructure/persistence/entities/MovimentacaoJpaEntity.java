package io.github.dudupuci.appdespesas.infrastructure.persistence.entities;

import io.github.dudupuci.appdespesas.domain.entities.Movimentacao;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base.JpaEntidadeUuid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "movimentacao")
@Getter
@Setter
public class MovimentacaoJpaEntity extends JpaEntidadeUuid {

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "data_da_movimentacao", nullable = false)
    private Date dataDaMovimentacao;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tipo_movimentacao", nullable = false)
    private TipoMovimentacao tipoMovimentacao;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaJpaEntity categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioSistemaJpaEntity usuarioSistema;

    @Column(name = "is_recorrente", nullable = false)
    private Boolean isRecorrente = false;

    public MovimentacaoJpaEntity() {
        super();
    }

    /**
     * Converte a entidade JPA para entidade de domínio
     */
    public Movimentacao toEntity() {
        Movimentacao movimentacao = new Movimentacao();
        super.toDomain(movimentacao);
        movimentacao.setTitulo(this.titulo);
        movimentacao.setDescricao(this.descricao);
        movimentacao.setValor(this.valor);
        movimentacao.setDataDaMovimentacao(this.dataDaMovimentacao);
        movimentacao.setTipoMovimentacao(this.tipoMovimentacao);
        movimentacao.setIsRecorrente(this.isRecorrente);

        if (this.categoria != null) {
            movimentacao.setCategoria(this.categoria.toEntity());
        }

        if (this.usuarioSistema != null) {
            movimentacao.setUsuarioSistema(this.usuarioSistema.toEntity());
        }

        return movimentacao;
    }

    /**
     * Cria uma entidade JPA a partir de uma entidade de domínio (sem relacionamentos)
     */
    public static MovimentacaoJpaEntity fromEntity(Movimentacao movimentacao) {
        if (movimentacao == null) return null;

        MovimentacaoJpaEntity movimentacaoJpaEntity = new MovimentacaoJpaEntity();
        movimentacaoJpaEntity.fromDomain(movimentacao);
        movimentacaoJpaEntity.setTitulo(movimentacao.getTitulo());
        movimentacaoJpaEntity.setDescricao(movimentacao.getDescricao());
        movimentacaoJpaEntity.setValor(movimentacao.getValor());
        movimentacaoJpaEntity.setDataDaMovimentacao(movimentacao.getDataDaMovimentacao());
        movimentacaoJpaEntity.setTipoMovimentacao(movimentacao.getTipoMovimentacao());
        movimentacaoJpaEntity.setIsRecorrente(movimentacao.getIsRecorrente());
        return movimentacaoJpaEntity;
    }

    /**
     * Atualiza a entidade JPA com dados da entidade de domínio
     */
    public void updateFromEntity(Movimentacao movimentacao) {
        this.titulo = movimentacao.getTitulo();
        this.descricao = movimentacao.getDescricao();
        this.valor = movimentacao.getValor();
        this.dataDaMovimentacao = movimentacao.getDataDaMovimentacao();
        this.tipoMovimentacao = movimentacao.getTipoMovimentacao();
        this.isRecorrente = movimentacao.getIsRecorrente();
        this.dataAtualizacao = movimentacao.getDataAtualizacao();
    }
}

