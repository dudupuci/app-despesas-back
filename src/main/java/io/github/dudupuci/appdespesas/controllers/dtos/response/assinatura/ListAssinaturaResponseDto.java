package io.github.dudupuci.appdespesas.controllers.dtos.response.assinatura;

import io.github.dudupuci.appdespesas.models.entities.Assinatura;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class ListAssinaturaResponseDto {
    private String nomePlano;
    private BigDecimal valor;
    private String descricao;
    private List<String> beneficios;

    public static ListAssinaturaResponseDto fromEntity(Assinatura assinatura) {
        ListAssinaturaResponseDto dto = new ListAssinaturaResponseDto();
        dto.setNomePlano(assinatura.getNomePlano());
        dto.setValor(assinatura.getValor());
        dto.setDescricao(assinatura.getDescricao());
        dto.setBeneficios(assinatura.getBeneficios());
        return dto;
    }
}
