package io.github.dudupuci.appdespesas.infrastructure.controllers.admin.dtos.request.usuarios;

import io.github.dudupuci.appdespesas.infrastructure.controllers.dtos.request.contato.AtualizarContatoRequestDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.dtos.request.endereco.AtualizarEnderecoRequestDto;
import io.github.dudupuci.appdespesas.application.services.annotations.CpfOuCnpj;

public record AtualizarUsuarioSistemaRequestDto(
        @CpfOuCnpj
        String cpfCnpj,

        AtualizarContatoRequestDto contatoDto,
        AtualizarEnderecoRequestDto enderecoDto
) {
}
