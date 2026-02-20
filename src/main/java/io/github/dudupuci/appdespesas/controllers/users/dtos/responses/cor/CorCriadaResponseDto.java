package io.github.dudupuci.appdespesas.controllers.users.dtos.responses.cor;

import io.github.dudupuci.appdespesas.controllers.dtos.base.ResponseDto;
import io.github.dudupuci.appdespesas.models.entities.Cor;
import io.github.dudupuci.appdespesas.utils.AppDespesasMessages;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CorCriadaResponseDto extends ResponseDto {
    private final UUID id;
    private final String nome;
    private final String codigoHexadecimal;

    public CorCriadaResponseDto(
            UUID id,
            String nome,
            String codigoHexadecimal,
            String mensagemKey
    ) {
        super(AppDespesasMessages.getMessage(mensagemKey, null));
        this.id = id;
        this.nome = nome;
        this.codigoHexadecimal = codigoHexadecimal;
    }

    public static CorCriadaResponseDto fromEntityCriada(Cor cor) {
        return fromEntity(cor, "cor.criada.sucesso");
    }

    private static CorCriadaResponseDto fromEntity(Cor cor, String mensagemKey) {
        return new CorCriadaResponseDto(
                cor.getId(),
                cor.getNome(),
                cor.getCodigoHexadecimal(),
                mensagemKey
        );
    }
}

