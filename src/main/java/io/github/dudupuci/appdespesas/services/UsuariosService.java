package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.controllers.admin.dtos.request.usuarios.AtualizarUsuarioSistemaRequestDto;
import io.github.dudupuci.appdespesas.controllers.dtos.request.endereco.AtualizarEnderecoRequestDto;
import io.github.dudupuci.appdespesas.models.entities.Contato;
import io.github.dudupuci.appdespesas.models.entities.Endereco;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.repositories.UsuariosRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UsuariosService {

    private final UsuariosRepository usuariosRepository;

    public UsuariosService(UsuariosRepository usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    public boolean existePorNomeUsuario(String nomeUsuario) {
        return this.usuariosRepository.existsByNomeUsuario(nomeUsuario);
    }

    public List<UsuarioSistema> listarUsuarios() {
        return this.usuariosRepository.findAll();
    }

    public UsuarioSistema buscarPorId(UUID id) {
        return this.usuariosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public UsuarioSistema atualizar(UUID usuarioIdLogado, AtualizarUsuarioSistemaRequestDto dto) {
        UsuarioSistema usuarioExistente = this.usuariosRepository.findById(usuarioIdLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Atualiza contato
        Contato contato = usuarioExistente.getContato();
        if (contato == null) contato = new Contato();
        if (dto.celular() != null && !dto.celular().isBlank()) contato.setCelular(dto.celular());
        if (dto.telefoneFixo() != null && !dto.telefoneFixo().isBlank()) contato.setTelefoneFixo(dto.telefoneFixo());
        usuarioExistente.setContato(contato);

        // Atualiza CPF/CNPJ
        if (dto.cpfOuCnpj() != null && !dto.cpfOuCnpj().isBlank()) {
            usuarioExistente.setCpfCnpj(dto.cpfOuCnpj());
        }

        // Atualiza endereco
        AtualizarEnderecoRequestDto enderecoDto = dto.enderecoDto();
        if (enderecoDto != null) {
            Endereco endereco = usuarioExistente.getEndereco();
            if (endereco == null) endereco = new Endereco();
            if (enderecoDto.logradouro() != null) endereco.setLogradouro(enderecoDto.logradouro());
            if (enderecoDto.numero() != null) endereco.setNumero(enderecoDto.numero());
            if (enderecoDto.complemento() != null) endereco.setComplemento(enderecoDto.complemento());
            if (enderecoDto.bairro() != null) endereco.setBairro(enderecoDto.bairro());
            if (enderecoDto.cep() != null) endereco.setCep(enderecoDto.cep());
            usuarioExistente.setEndereco(endereco);
        }

        usuarioExistente.setDataAtualizacao(new Date());
        return this.usuariosRepository.save(usuarioExistente);
    }
}
