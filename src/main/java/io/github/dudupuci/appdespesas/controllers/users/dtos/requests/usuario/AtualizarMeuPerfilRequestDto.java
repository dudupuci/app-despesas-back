package io.github.dudupuci.appdespesas.controllers.users.dtos.requests.usuario;

import io.github.dudupuci.appdespesas.controllers.dtos.request.contato.AtualizarContatoRequestDto;
import io.github.dudupuci.appdespesas.controllers.dtos.request.endereco.AtualizarEnderecoRequestDto;
import io.github.dudupuci.appdespesas.services.annotations.CpfOuCnpj;

import javax.validation.Valid;

public record AtualizarMeuPerfilRequestDto(
        @CpfOuCnpj
        String cpfCnpj,
        AtualizarContatoRequestDto contatoDto,
        AtualizarEnderecoRequestDto enderecoDto
) {
}
