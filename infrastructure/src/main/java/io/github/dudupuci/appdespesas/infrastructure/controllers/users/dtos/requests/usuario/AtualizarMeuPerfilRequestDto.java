package io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.usuario;

import io.github.dudupuci.appdespesas.application.usecases.usuario.AtualizarUsuarioCommand;
import io.github.dudupuci.appdespesas.infrastructure.controllers.dtos.request.contato.AtualizarContatoRequestDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.dtos.request.endereco.AtualizarEnderecoRequestDto;
import io.github.dudupuci.appdespesas.domain.annotations.CpfOuCnpj;

public record AtualizarMeuPerfilRequestDto(
        @CpfOuCnpj
        String cpfCnpj,
        AtualizarContatoRequestDto contatoDto,
        AtualizarEnderecoRequestDto enderecoDto
) {
    public AtualizarUsuarioCommand toCommand() {
        AtualizarUsuarioCommand.ContatoCommand contato = contatoDto != null
                ? new AtualizarUsuarioCommand.ContatoCommand(contatoDto.telefoneFixo(), contatoDto.celular())
                : null;
        AtualizarUsuarioCommand.EnderecoCommand endereco = enderecoDto != null
                ? new AtualizarUsuarioCommand.EnderecoCommand(
                        enderecoDto.logradouro(), enderecoDto.numero(),
                        enderecoDto.complemento(), enderecoDto.bairro(), enderecoDto.cep())
                : null;
        return new AtualizarUsuarioCommand(cpfCnpj, contato, endereco);
    }
}
