package io.github.dudupuci.appdespesas.infrastructure.controllers.dtos.request.endereco;

public record AtualizarEnderecoRequestDto(
         String logradouro,
         String numero,
         String complemento,
         String bairro,
         String cep
) {
}
