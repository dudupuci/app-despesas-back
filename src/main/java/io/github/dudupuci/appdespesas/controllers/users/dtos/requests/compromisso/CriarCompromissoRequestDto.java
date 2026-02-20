package io.github.dudupuci.appdespesas.controllers.users.dtos.requests.compromisso;

import io.github.dudupuci.appdespesas.models.entities.Compromisso;
import io.github.dudupuci.appdespesas.models.enums.Prioridade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record CriarCompromissoRequestDto(
        @NotBlank(message = "Título é obrigatório")
        String titulo,
        String descricao,
        @NotNull(message = "Data de início é obrigatória")
        Date dataInicio,
        Date dataFim,
        @NotNull(message = "Campo 'dia inteiro' é obrigatório")
        Boolean diaInteiro,
        Prioridade prioridade,
        String localizacao,
        String cor,
        String observacoes
) {

    public Compromisso toCompromisso() {
        Compromisso compromisso = new Compromisso();
        compromisso.setTitulo(titulo);
        compromisso.setDescricao(descricao);
        compromisso.setDataInicio(dataInicio);
        compromisso.setDataFim(dataFim);
        compromisso.setDiaInteiro(diaInteiro);
        compromisso.setPrioridade(prioridade);
        compromisso.setLocalizacao(localizacao);
        compromisso.setCor(cor);
        compromisso.setObservacoes(observacoes);
        return compromisso;
    }
}

