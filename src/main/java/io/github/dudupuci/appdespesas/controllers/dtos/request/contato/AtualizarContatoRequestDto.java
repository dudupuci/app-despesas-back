package io.github.dudupuci.appdespesas.controllers.dtos.request.contato;

import org.hibernate.validator.constraints.Length;

public record AtualizarContatoRequestDto(
        @Length(min = 10, max = 11, message = "Telefone deve conter 10 ou 11 dígitos")
        String telefoneFixo,

        @Length(min = 11, max = 11, message = "Celular deve conter exatamente 11 dígitos")
        String celular
) {
}
