package io.github.dudupuci.appdespesas.controllers.dtos.request.endereco;

public record AtualizarEnderecoRequestDto(
         String logradouro,
         String numero,
         String complemento,
         String bairro,
         String cep
) {
}
