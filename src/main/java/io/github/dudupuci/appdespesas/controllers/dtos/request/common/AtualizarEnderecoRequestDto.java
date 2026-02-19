package io.github.dudupuci.appdespesas.controllers.dtos.request.common;

import jakarta.persistence.Column;

public record AtualizarEnderecoRequestDto(
         String logradouro,
         String numero,
         String complemento,
         String bairro,
         String cep
) {
}
