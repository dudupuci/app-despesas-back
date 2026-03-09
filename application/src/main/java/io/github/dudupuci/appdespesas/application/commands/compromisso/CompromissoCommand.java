package io.github.dudupuci.appdespesas.application.commands.compromisso;

import io.github.dudupuci.appdespesas.domain.enums.Prioridade;

import java.util.Date;

/**
 * Command para criar ou atualizar um compromisso.
 */
public record CompromissoCommand(
        String titulo,
        String descricao,
        Date dataInicio,
        Date dataFim,
        Boolean diaInteiro,
        Prioridade prioridade,
        String localizacao,
        String cor,
        String observacoes
) {}

