package io.github.dudupuci.appdespesas.application.services;


import io.github.dudupuci.appdespesas.application.ports.repositories.UsuarioRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.Contato;
import io.github.dudupuci.appdespesas.domain.entities.Endereco;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.exceptions.EntityNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UsuarioService {

    private final UsuarioRepositoryPort usuariosRepository;

    public UsuarioService(UsuarioRepositoryPort usuariosRepository) {
        this.usuariosRepository = usuariosRepository;
    }

    public boolean existePorNomeUsuario(String nomeUsuario) {
        return this.usuariosRepository.existsByNomeUsuario(nomeUsuario);
    }

    public UsuarioSistema buscarPorId(UUID id) {
        return this.usuariosRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public UsuarioSistema buscarPorEmail(String email) {
        return this.usuariosRepository.buscarPorEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário de email " + email + " não encontrado."));
    }

    public List<UsuarioSistema> listarUsuarios() {
        return this.usuariosRepository.findAll();
    }

    public UsuarioSistema atualizar(UUID usuarioId, io.github.dudupuci.appdespesas.application.usecases.usuario.AtualizarUsuarioCommand cmd) {
        UsuarioSistema usuario = buscarPorId(usuarioId);

        if (cmd.cpfCnpj() != null && !cmd.cpfCnpj().isBlank()) {
            usuario.setCpfCnpj(cmd.cpfCnpj());
        }

        if (cmd.contato() != null) {
            Contato contato = usuario.getContato() != null ? usuario.getContato() : new Contato();
            if (cmd.contato().celular() != null && !cmd.contato().celular().isBlank())
                contato.setCelular(cmd.contato().celular());
            if (cmd.contato().telefoneFixo() != null && !cmd.contato().telefoneFixo().isBlank())
                contato.setTelefoneFixo(cmd.contato().telefoneFixo());
            usuario.setContato(contato);
        }

        if (cmd.endereco() != null) {
            Endereco endereco = usuario.getEndereco() != null ? usuario.getEndereco() : new Endereco();
            if (cmd.endereco().logradouro() != null) endereco.setLogradouro(cmd.endereco().logradouro());
            if (cmd.endereco().numero() != null) endereco.setNumero(cmd.endereco().numero());
            if (cmd.endereco().complemento() != null) endereco.setComplemento(cmd.endereco().complemento());
            if (cmd.endereco().bairro() != null) endereco.setBairro(cmd.endereco().bairro());
            if (cmd.endereco().cep() != null) endereco.setCep(cmd.endereco().cep());
            usuario.setEndereco(endereco);
        }

        usuario.setDataAtualizacao(new Date());
        return this.usuariosRepository.save(usuario);
    }

    public void atualizar(UsuarioSistema usuarioSistema) {
        usuarioSistema.setDataAtualizacao(new Date());
        this.usuariosRepository.save(usuarioSistema);
    }
}
