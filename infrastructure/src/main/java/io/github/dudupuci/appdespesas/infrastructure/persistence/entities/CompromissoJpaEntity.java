package io.github.dudupuci.appdespesas.infrastructure.persistence.entities;

import io.github.dudupuci.appdespesas.domain.entities.Compromisso;
import io.github.dudupuci.appdespesas.domain.enums.Prioridade;
import io.github.dudupuci.appdespesas.infrastructure.persistence.entities.base.JpaEntidadeUuid;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "compromisso")
@Getter
@Setter
public class CompromissoJpaEntity extends JpaEntidadeUuid {

    // Campos de Evento
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
    protected Boolean diaInteiro;

    // Campos específicos de Compromisso
    @Column(name = "lembrar_em")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lembrarEm;

    @Column(length = 50)
    private String cor;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioSistemaJpaEntity usuarioSistema;

    @Column(length = 500)
    private String observacoes;

    public CompromissoJpaEntity() {
        super();
    }

    /**
     * Converte a entidade JPA para entidade de domínio
     */
    public Compromisso toEntity() {
        Compromisso compromisso = new Compromisso();
        super.toDomain(compromisso);

        // Campos de Evento
        compromisso.setTitulo(this.titulo);
        compromisso.setDescricao(this.descricao);
        compromisso.setLocalizacao(this.localizacao);
        compromisso.setDataInicio(this.dataInicio);
        compromisso.setDataFim(this.dataFim);
        compromisso.setPrioridade(this.prioridade);
        compromisso.setConcluido(this.concluido);
        compromisso.setDataConclusao(this.dataConclusao);
        compromisso.setDiaInteiro(this.diaInteiro);

        // Campos específicos
        compromisso.setLembrarEm(this.lembrarEm);
        compromisso.setCor(this.cor);
        compromisso.setObservacoes(this.observacoes);

        if (this.usuarioSistema != null) {
            compromisso.setUsuarioSistema(this.usuarioSistema.toEntity());
        }

        return compromisso;
    }

    /**
     * Cria uma entidade JPA a partir de uma entidade de domínio (sem relacionamentos)
     */
    public static CompromissoJpaEntity fromEntity(Compromisso compromisso) {
        if (compromisso == null) return null;

        CompromissoJpaEntity compromissoJpaEntity = new CompromissoJpaEntity();
        compromissoJpaEntity.fromDomain(compromisso);

        // Campos de Evento
        compromissoJpaEntity.setTitulo(compromisso.getTitulo());
        compromissoJpaEntity.setDescricao(compromisso.getDescricao());
        compromissoJpaEntity.setLocalizacao(compromisso.getLocalizacao());
        compromissoJpaEntity.setDataInicio(compromisso.getDataInicio());
        compromissoJpaEntity.setDataFim(compromisso.getDataFim());
        compromissoJpaEntity.setPrioridade(compromisso.getPrioridade());
        compromissoJpaEntity.setConcluido(compromisso.getConcluido());
        compromissoJpaEntity.setDataConclusao(compromisso.getDataConclusao());
        compromissoJpaEntity.setDiaInteiro(compromisso.getDiaInteiro());

        // Campos específicos
        compromissoJpaEntity.setLembrarEm(compromisso.getLembrarEm());
        compromissoJpaEntity.setCor(compromisso.getCor());
        compromissoJpaEntity.setObservacoes(compromisso.getObservacoes());

        return compromissoJpaEntity;
    }

    /**
     * Atualiza a entidade JPA com dados da entidade de domínio
     */
    public void updateFromEntity(Compromisso compromisso) {
        this.titulo = compromisso.getTitulo();
        this.descricao = compromisso.getDescricao();
        this.localizacao = compromisso.getLocalizacao();
        this.dataInicio = compromisso.getDataInicio();
        this.dataFim = compromisso.getDataFim();
        this.prioridade = compromisso.getPrioridade();
        this.concluido = compromisso.getConcluido();
        this.dataConclusao = compromisso.getDataConclusao();
        this.diaInteiro = compromisso.getDiaInteiro();
        this.lembrarEm = compromisso.getLembrarEm();
        this.cor = compromisso.getCor();
        this.observacoes = compromisso.getObservacoes();
        this.dataAtualizacao = compromisso.getDataAtualizacao();
    }
}

