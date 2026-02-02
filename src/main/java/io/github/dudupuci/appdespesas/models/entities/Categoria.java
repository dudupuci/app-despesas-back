package io.github.dudupuci.appdespesas.models.entities;

import io.github.dudupuci.appdespesas.models.entities.base.Entidade;
import io.github.dudupuci.appdespesas.models.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categorias")
@Getter
@Setter
public class Categoria extends Entidade {
    private String nome;
    private String descricao;
    private Status status;

    @OneToMany(mappedBy = "categoria")
    private Set<Movimentacao> movimentacoes;

    public Categoria() {}

    public Categoria(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.movimentacoes = new HashSet<>();
        this.status = Status.ATIVO;
    }
}
