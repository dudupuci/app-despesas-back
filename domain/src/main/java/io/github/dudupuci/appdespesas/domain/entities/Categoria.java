package io.github.dudupuci.appdespesas.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.dudupuci.appdespesas.domain.entities.base.EntidadeUuid;
import io.github.dudupuci.appdespesas.domain.enums.Status;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria extends EntidadeUuid {
    private String nome;
    private String descricao;
    private Status status;
    private TipoMovimentacao tipoMovimentacao;

    //@JsonIgnore
    private Set<Movimentacao> movimentacoes = new HashSet<>();

    //@JsonIgnore
    private UsuarioSistema usuarioSistema;

    private Cor cor;


    public Categoria(String nome, String descricao, TipoMovimentacao tipoMovimentacao) {
        this.nome = nome;
        this.descricao = descricao;
        this.tipoMovimentacao = tipoMovimentacao;
        this.movimentacoes = new HashSet<>();
        this.status = Status.ATIVO;
    }

}
