package io.github.dudupuci.appdespesas.controllers.dtos.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.dudupuci.appdespesas.config.app.DateSerializer;
import io.github.dudupuci.appdespesas.controllers.dtos.base.ResponseDto;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
public class MovimentacaoCriadaDto extends ResponseDto {
    private final String titulo;
    private final String descricao;
    private final BigDecimal valor;
    @JsonSerialize(using = DateSerializer.class)
    private final Date dataDaMovimentacao;
    private final String tipoMovimentacao;
    private final String nomeCategoria;

    public MovimentacaoCriadaDto(
            String titulo,
            String descricao,
            BigDecimal valor,
            Date dataDaMovimentacao,
            String tipoMovimentacao,
            String nomeCategoria,
            String mensagemKey
    ) {
        super(AppDespesasMessages.getMessage(mensagemKey, null));
        this.titulo = titulo;
        this.descricao = descricao;
        this.valor = valor;
        this.dataDaMovimentacao = dataDaMovimentacao;
        this.tipoMovimentacao = tipoMovimentacao;
        this.nomeCategoria = nomeCategoria;
    }

    public static MovimentacaoCriadaDto fromEntityCriada(Movimentacao movimentacao) {
        return fromEntity(movimentacao, "movimentacao.criada.sucesso");
    }

    private static MovimentacaoCriadaDto fromEntity(Movimentacao movimentacao, String mensagemKey) {
        return new MovimentacaoCriadaDto(
                movimentacao.getTitulo(),
                movimentacao.getDescricao(),
                movimentacao.getValor(),
                movimentacao.getDataDaMovimentacao(),
                movimentacao.getTipoMovimentacao().name(),
                movimentacao.getCategoria() != null ? movimentacao.getCategoria().getNome() : null,
                mensagemKey
        );
    }

}
