package io.github.dudupuci.appdespesas.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.dudupuci.appdespesas.models.entities.base.Entidade;
import io.github.dudupuci.appdespesas.models.entities.base.EntidadeUuid;
import io.github.dudupuci.appdespesas.models.enums.PrioridadeEvento;
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
public class Compromisso extends EntidadeUuid {

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(length = 1000)
    private String descricao;

    @Column(name = "data_inicio", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicio;

    @Column(name = "data_fim")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFim;

    @Column(name = "dia_inteiro")
    private Boolean diaInteiro = false;

    @Enumerated(EnumType.STRING)
    private PrioridadeEvento prioridade;

    @Column(length = 500)
    private String localizacao;

    @Column(nullable = false)
    private Boolean concluido = false;

    @Column(name = "data_conclusao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataConclusao;

    @Column(name = "lembrar_em")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lembrarEm;

    @Column(length = 50)
    private String cor; // Cor hexadecimal para exibição no calendário (ex: #FF5733)

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private UsuarioSistema usuarioSistema;

    @Column(length = 500)
    private String observacoes;

    public Compromisso() {}

    public Compromisso(String titulo, Date dataInicio, UsuarioSistema usuarioSistema) {
        this.titulo = titulo;
        this.dataInicio = dataInicio;
        this.usuarioSistema = usuarioSistema;
    }
}

