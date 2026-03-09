package io.github.dudupuci.appdespesas.infrastructure.controllers.noauth.dtos.request.login;

import io.github.dudupuci.appdespesas.application.commands.auth.LoginCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
        @NotBlank(message = "E-mail é obrigatório")
        @Email(message = "E-mail inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        String senha
) {
    public LoginCommand toCommand() {
        return new LoginCommand(email, senha);
    }
}

