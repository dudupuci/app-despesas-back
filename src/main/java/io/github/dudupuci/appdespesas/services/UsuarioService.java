package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.controllers.admin.dtos.request.usuarios.AtualizarUsuarioSistemaRequestDto;
import io.github.dudupuci.appdespesas.controllers.dtos.request.endereco.AtualizarEnderecoRequestDto;
import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.usuario.AtualizarMeuPerfilRequestDto;
import io.github.dudupuci.appdespesas.exceptions.EntityNotFoundException;
import io.github.dudupuci.appdespesas.models.entities.Contato;
import io.github.dudupuci.appdespesas.models.entities.Endereco;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.repositories.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuariosRepository usuariosRepository;

    public UsuarioService(UsuariosRepository usuariosRepository) {
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
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public UsuarioSistema buscarPorEmail(String email) {
        return this.usuariosRepository.buscarPorEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário de email " + email + " não encontrado."));
    }


    // Endpoint admin
    public UsuarioSistema atualizar(UUID usuarioIdLogado, AtualizarUsuarioSistemaRequestDto dto) {
        UsuarioSistema usuarioExistente = buscarPorId(usuarioIdLogado);

        // Atualiza contato
        Contato contato = usuarioExistente.getContato();
        if (contato == null) contato = new Contato();
        if (dto.contatoDto().celular() != null && !dto.contatoDto().celular().isBlank())
            contato.setCelular(dto.contatoDto().celular());
        if (dto.contatoDto().telefoneFixo() != null && !dto.contatoDto().telefoneFixo().isBlank())
            contato.setTelefoneFixo(dto.contatoDto().telefoneFixo());
        usuarioExistente.setContato(contato);

        // Atualiza CPF/CNPJ
        if (dto.cpfCnpj() != null && !dto.cpfCnpj().isBlank()) {
            usuarioExistente.setCpfCnpj(dto.cpfCnpj());
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

    // Endpoint usuario
    public UsuarioSistema atualizar(UUID usuarioIdLogado, AtualizarMeuPerfilRequestDto dto) {
        UsuarioSistema usuarioExistente = buscarPorId(usuarioIdLogado);

        // Atualiza contato
        Contato contato = usuarioExistente.getContato();
        if (contato == null) contato = new Contato();
        if (dto.contatoDto().celular() != null && !dto.contatoDto().celular().isBlank())
            contato.setCelular(dto.contatoDto().celular());
        if (dto.contatoDto().telefoneFixo() != null && !dto.contatoDto().telefoneFixo().isBlank())
            contato.setTelefoneFixo(dto.contatoDto().telefoneFixo());
        usuarioExistente.setContato(contato);

        // Atualiza CPF/CNPJ
        if (dto.cpfCnpj() != null && !dto.cpfCnpj().isBlank()) {
            usuarioExistente.setCpfCnpj(dto.cpfCnpj());
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


    public void atualizar(UsuarioSistema usuarioSistema) {
        usuarioSistema.setDataAtualizacao(new Date());
        this.usuariosRepository.save(usuarioSistema);
    }

}
