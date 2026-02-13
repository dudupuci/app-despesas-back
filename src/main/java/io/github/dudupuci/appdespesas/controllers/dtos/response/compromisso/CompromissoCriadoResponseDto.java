package io.github.dudupuci.appdespesas.controllers.dtos.response.compromisso;

import io.github.dudupuci.appdespesas.controllers.dtos.base.ResponseDto;
import io.github.dudupuci.appdespesas.models.entities.Compromisso;
import io.github.dudupuci.appdespesas.models.enums.Prioridade;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
public class CompromissoCriadoResponseDto extends ResponseDto {
    private final UUID id;
    private final String titulo;
    private final String descricao;
    private final Date dataInicio;
    private final Date dataFim;
    private final Boolean diaInteiro;
    private final Prioridade prioridade;
    private final String localizacao;
    private final String cor;

    public CompromissoCriadoResponseDto(
            UUID id,
            String titulo,
            String descricao,
            Date dataInicio,
            Date dataFim,
            Boolean diaInteiro,
            Prioridade prioridade,
            String localizacao,
            String cor,
            String mensagemKey
    ) {
        super(AppDespesasMessages.getMessage(mensagemKey, null));
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.diaInteiro = diaInteiro;
        this.prioridade = prioridade;
        this.localizacao = localizacao;
        this.cor = cor;
    }

    public static CompromissoCriadoResponseDto fromEntityCriado(Compromisso compromisso) {
        return fromEntity(compromisso, "compromisso.criado.sucesso");
    }

    private static CompromissoCriadoResponseDto fromEntity(Compromisso compromisso, String mensagemKey) {
        return new CompromissoCriadoResponseDto(
                compromisso.getId(),
                compromisso.getTitulo(),
                compromisso.getDescricao(),
                compromisso.getDataInicio(),
                compromisso.getDataFim(),
                compromisso.getDiaInteiro(),
                compromisso.getPrioridade(),
                compromisso.getLocalizacao(),
                compromisso.getCor(),
                mensagemKey
        );
    }
}

