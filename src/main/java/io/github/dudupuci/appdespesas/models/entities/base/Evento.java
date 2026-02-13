package io.github.dudupuci.appdespesas.models.entities.base;

import io.github.dudupuci.appdespesas.models.enums.Prioridade;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Classe base para entidades que representam eventos do usuário
 * Exemplos: Compromisso, Lembrete, Tarefa, etc.
 */
@MappedSuperclass
@Getter
@Setter
public abstract class Evento extends EntidadeUuid {
    protected String titulo;
    protected String descricao;
    protected String localizacao;

    @Column(name = "data_inicio", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dataInicio;

    @Column(name = "data_fim", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dataFim;

    @Enumerated(EnumType.STRING)
    protected Prioridade prioridade;

    @Column(nullable = false)
    protected Boolean concluido = false;

    @Column(name = "data_conclusao")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date dataConclusao;

    @Column(name = "dia_inteiro")
    protected Boolean diaInteiro = false;
}
