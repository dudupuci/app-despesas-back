package io.github.dudupuci.appdespesas.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.dudupuci.appdespesas.models.entities.base.EntidadeUuid;
import io.github.dudupuci.appdespesas.models.entities.base.Evento;
import io.github.dudupuci.appdespesas.models.enums.Prioridade;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Entidade que representa um compromisso/afazer do usuário
 * Exemplos: reuniões, consultas médicas, tarefas, lembretes, etc.
 */
@Entity
@Table(name = "compromissos")
@Getter
@Setter
public class Compromisso extends Evento {

    @Column(name = "lembrar_em")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lembrarEm;

    @Column(length = 50)
    private String cor;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private UsuarioSistema usuarioSistema;

    @Column(length = 500)
    private String observacoes;

    public Compromisso() {}

    public Compromisso(String titulo, String descricao, String localizacao, Date dataInicio, Date dataFim, Prioridade prioridade, Boolean diaInteiro, Date lembrarEm, String cor, UsuarioSistema usuarioSistema, String observacoes) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.prioridade = prioridade;
        this.diaInteiro = diaInteiro;
        this.lembrarEm = lembrarEm;
        this.cor = cor;
        this.usuarioSistema = usuarioSistema;
        this.observacoes = observacoes;
    }

}

