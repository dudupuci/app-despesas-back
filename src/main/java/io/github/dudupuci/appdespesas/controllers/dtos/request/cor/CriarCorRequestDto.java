package io.github.dudupuci.appdespesas.controllers.dtos.request.cor;

import io.github.dudupuci.appdespesas.models.entities.Cor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CriarCorRequestDto(
        @NotBlank(message = "Nome da cor é obrigatório")
        @Size(max = 100, message = "Nome da cor deve ter no máximo 100 caracteres")
        String nome,

        @NotBlank(message = "Código hexadecimal é obrigatório")
        @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Código hexadecimal inválido. Use o formato #RRGGBB")
        String codigoHexadecimal
) {

    public Cor toCor() {
        Cor cor = new Cor();
        cor.setNome(nome);
        cor.setCodigoHexadecimal(codigoHexadecimal.toUpperCase());
        return cor;
    }
}

