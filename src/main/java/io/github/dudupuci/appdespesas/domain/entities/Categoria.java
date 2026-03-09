package io.github.dudupuci.appdespesas.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeUuid;
import io.github.dudupuci.appdespesas.domain.enums.Status;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categoria")
@Getter
@Setter
public class Categoria extends EntidadeUuid {
    private String nome;
    private String descricao;
    private Status status;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tipo_movimentacao", nullable = false)
    private TipoMovimentacao tipoMovimentacao;

    @OneToMany(mappedBy = "categoria")
    //@JsonManagedReference
    @JsonIgnore
    private Set<Movimentacao> movimentacoes;

    // Relacionamento OPCIONAL com UsuarioSistema (para categorias criadas por usuários)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    @JsonIgnore
    private UsuarioSistema usuarioSistema;

    @ManyToOne
    @JoinColumn(name = "cor_id")
    private Cor cor;

    public Categoria() {}

    public Categoria(String nome, String descricao, TipoMovimentacao tipoMovimentacao) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipoMovimentacao = tipoMovimentacao;
        this.movimentacoes = new HashSet<>();
        this.status = Status.ATIVO;
    }

}
