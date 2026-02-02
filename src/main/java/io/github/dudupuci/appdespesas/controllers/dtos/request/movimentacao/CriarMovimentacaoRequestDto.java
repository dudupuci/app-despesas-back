package io.github.dudupuci.appdespesas.controllers.dtos.request.movimentacao;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.dudupuci.appdespesas.config.app.DateDeserializer;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;

import java.math.BigDecimal;
import java.util.Date;

public record CriarMovimentacaoRequestDto(
        String titulo,
        String descricao,
        BigDecimal valor,
        @JsonDeserialize(using = DateDeserializer.class)
        Date dataDaMovimentacao,
        TipoMovimentacao tipoMovimentacao,
        Long categoriaId
) {
    public Movimentacao toMovimentacao() {
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setTitulo(this.titulo);
        movimentacao.setDescricao(this.descricao);
        movimentacao.setValor(this.valor);
        movimentacao.setDataDaMovimentacao(this.dataDaMovimentacao);
        movimentacao.setTipoMovimentacao(this.tipoMovimentacao);
        return movimentacao;
    }
}
