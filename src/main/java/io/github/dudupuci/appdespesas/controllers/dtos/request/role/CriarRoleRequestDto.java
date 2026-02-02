package io.github.dudupuci.appdespesas.controllers.dtos.request.role;

import jakarta.validation.constraints.NotBlank;

public record CriarRoleRequestDto(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Descricao é obrigatório")
        String descricao,

        @NotBlank(message = "Nível de poder é obrigatório")
        Integer poder
) {
}
