package io.github.dudupuci.appdespesas.domain.entities.base;

import io.github.dudupuci.appdespesas.domain.enums.Prioridade;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Classe base para entidades que representam eventos do usuário
 * Exemplos: Compromisso, Lembrete, Tarefa, etc.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Evento extends EntidadeUuid {
    protected String titulo;
    protected String descricao;
    protected String localizacao;
    protected Date dataInicio;
    protected Date dataFim;
    protected Prioridade prioridade;
    protected Boolean concluido = false;
    protected Date dataConclusao;
    protected Boolean diaInteiro;
}
