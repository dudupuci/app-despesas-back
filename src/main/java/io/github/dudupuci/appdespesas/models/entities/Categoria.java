package io.github.dudupuci.appdespesas.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.github.dudupuci.appdespesas.models.entities.base.Entidade;
import io.github.dudupuci.appdespesas.models.entities.base.EntidadeUuid;
import io.github.dudupuci.appdespesas.models.enums.Status;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias")
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

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private UsuarioSistema usuarioSistema;

    public Categoria() {}

    public Categoria(String nome, String descricao, TipoMovimentacao tipoMovimentacao) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipoMovimentacao = tipoMovimentacao;
        this.movimentacoes = new HashSet<>();
        this.status = Status.ATIVO;
    }
}
