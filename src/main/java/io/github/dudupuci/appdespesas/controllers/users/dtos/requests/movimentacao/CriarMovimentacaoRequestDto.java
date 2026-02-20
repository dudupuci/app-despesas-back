package io.github.dudupuci.appdespesas.controllers.users.dtos.requests.movimentacao;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.dudupuci.appdespesas.config.app.DateDeserializer;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public record CriarMovimentacaoRequestDto(
        @NotBlank(message = "Título é obrigatório")
        String titulo,
        String descricao,
        @NotNull(message = "Valor é obrigatório")
        BigDecimal valor,
        @NotNull(message = "Data da movimentação é obrigatória")
        @JsonDeserialize(using = DateDeserializer.class)
        Date dataDaMovimentacao,
        @NotNull(message = "Tipo de movimentação é obrigatório")
        TipoMovimentacao tipoMovimentacao,
        @NotNull(message = "Categoria é obrigatória")
        UUID categoriaId
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
