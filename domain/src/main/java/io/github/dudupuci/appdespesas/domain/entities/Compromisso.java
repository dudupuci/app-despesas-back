package io.github.dudupuci.appdespesas.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.dudupuci.appdespesas.domain.entities.base.Evento;
import io.github.dudupuci.appdespesas.domain.enums.Prioridade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Entidade que representa um compromisso/afazer do usuário
 * Exemplos: reuniões, consultas médicas, tarefas, lembretes, etc.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Compromisso extends Evento {

    private Date lembrarEm;
    private String cor;

    //@JsonIgnore
    private UsuarioSistema usuarioSistema;
    private String observacoes;

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

