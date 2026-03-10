package io.github.dudupuci.appdespesas.infrastructure.controllers.admin.dtos.request.role;


import io.github.dudupuci.appdespesas.application.usecases.role.CriarRoleCommand;
import jakarta.validation.constraints.NotBlank;

public record CriarRoleRequestDto(
        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        Integer poder
) {
    public CriarRoleCommand toCommand() {
        return new CriarRoleCommand(nome, descricao, poder);
    }
}
