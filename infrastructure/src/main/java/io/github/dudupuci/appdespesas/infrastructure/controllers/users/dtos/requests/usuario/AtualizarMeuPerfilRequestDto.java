package io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.usuario;

import io.github.dudupuci.appdespesas.infrastructure.controllers.dtos.request.contato.AtualizarContatoRequestDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.dtos.request.endereco.AtualizarEnderecoRequestDto;
import io.github.dudupuci.appdespesas.domain.annotations.CpfOuCnpj;

public record AtualizarMeuPerfilRequestDto(
        @CpfOuCnpj
        String cpfCnpj,
        AtualizarContatoRequestDto contatoDto,
        AtualizarEnderecoRequestDto enderecoDto
) {
}
