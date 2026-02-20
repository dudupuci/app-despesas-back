package io.github.dudupuci.appdespesas.controllers.admin.dtos.request.usuarios;

import io.github.dudupuci.appdespesas.controllers.dtos.request.contato.AtualizarContatoRequestDto;
import io.github.dudupuci.appdespesas.controllers.dtos.request.endereco.AtualizarEnderecoRequestDto;
import io.github.dudupuci.appdespesas.services.annotations.CpfOuCnpj;

public record AtualizarUsuarioSistemaRequestDto(
        @CpfOuCnpj
        String cpfOuCnpj,

        AtualizarContatoRequestDto contatoDto,
        AtualizarEnderecoRequestDto enderecoDto
) {
}
