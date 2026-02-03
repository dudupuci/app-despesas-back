package io.github.dudupuci.appdespesas.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.dudupuci.appdespesas.models.entities.base.Entidade;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "movimentacoes")
@Getter
@Setter
public class Movimentacao extends Entidade {

    public Movimentacao(){}

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
    @JsonIgnore
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private UsuarioSistema usuarioSistema;

    @JsonProperty("categoriaId")
    public Long getCategoriaId() {
        return categoria != null ? categoria.getId() : null;
    }

    @JsonProperty("categoriaNome")
    public String getCategoriaNome() {
        return categoria != null ? categoria.getNome() : null;
    }

}
