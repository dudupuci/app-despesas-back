package io.github.dudupuci.appdespesas.application.usecases.compromisso;

import io.github.dudupuci.appdespesas.domain.enums.Prioridade;

import java.util.Date;
import java.util.UUID;

/**
 * Command para criar ou atualizar um compromisso.
 */
public record CompromissoCommand(
        UUID usuarioId,
        UUID compromissoId, // usado apenas no update/delete/concluir
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
