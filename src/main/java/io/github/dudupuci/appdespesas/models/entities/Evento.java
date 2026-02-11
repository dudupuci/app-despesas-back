package io.github.dudupuci.appdespesas.models.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.dudupuci.appdespesas.models.entities.base.Entidade;
import io.github.dudupuci.appdespesas.models.entities.base.EntidadeUuid;
import io.github.dudupuci.appdespesas.models.enums.FrequenciaRecorrencia;
import io.github.dudupuci.appdespesas.models.enums.PrioridadeEvento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Entidade que representa eventos recorrentes do calendário
 * Exemplos: aniversários, pagamentos fixos, salário, etc.
 */
@Entity
@Table(name = "eventos")
@Getter
@Setter
public class Evento extends EntidadeUuid {

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(name = "data_inicio", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInicio;

    @Column(name = "data_fim")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFim;

    @Column(length = 1000)
    private String descricao;

    @Column(name = "is_recorrente", nullable = false)
    private Boolean isRecorrente = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequencia_recorrencia")
    private FrequenciaRecorrencia frequenciaRecorrencia;

    @Column(name = "data_fim_recorrencia")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataFimRecorrencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioridadeEvento prioridade = PrioridadeEvento.MEDIA;

    @Column(length = 50)
    private String cor; // Cor hexadecimal para exibição no calendário

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private UsuarioSistema usuarioSistema;

    @Column(name = "dia_inteiro")
    private Boolean diaInteiro = false;

    @Column(length = 500)
    private String observacoes;

    public Evento() {}

    public Evento(String titulo, Date dataInicio, Boolean isRecorrente, UsuarioSistema usuarioSistema) {
        this.titulo = titulo;
        this.dataInicio = dataInicio;
        this.isRecorrente = isRecorrente;
        this.usuarioSistema = usuarioSistema;
    }
}
