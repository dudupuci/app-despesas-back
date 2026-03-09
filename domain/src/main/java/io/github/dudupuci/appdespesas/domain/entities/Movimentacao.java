package io.github.dudupuci.appdespesas.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeUuid;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Movimentacao extends EntidadeUuid {

    private String titulo;
    private String descricao;
    private BigDecimal valor;
    private Date dataDaMovimentacao;
    private TipoMovimentacao tipoMovimentacao;

    @JsonIgnore
    private Categoria categoria;

    @JsonIgnore
    private UsuarioSistema usuarioSistema;

    private Boolean isRecorrente = false;


    @JsonProperty("categoriaId")
    public UUID getCategoriaId() {
        return categoria != null ? categoria.getId() : null;
    }

    @JsonProperty("categoriaNome")
    public String getCategoriaNome() {
        return categoria != null ? categoria.getNome() : null;
    }

}
