package io.github.dudupuci.appdespesas.controllers.noauth.dtos.response.assinatura;

import io.github.dudupuci.appdespesas.models.entities.Assinatura;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class AssinaturaResponseDto {
    private Long id;
    private String nomePlano;
    private BigDecimal valor;
    private String descricao;
    private List<String> beneficios;

    public static AssinaturaResponseDto fromEntity(Assinatura assinatura) {
        AssinaturaResponseDto dto = new AssinaturaResponseDto();
        dto.setId(assinatura.getId());
        dto.setNomePlano(assinatura.getNomePlano());
        dto.setValor(assinatura.getValor());
        dto.setDescricao(assinatura.getDescricao());
        dto.setBeneficios(assinatura.getBeneficios());
        return dto;
    }
}
