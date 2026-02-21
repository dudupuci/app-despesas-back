package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.controllers.admin.dtos.request.usuarios.AtualizarUsuarioSistemaRequestDto;
import io.github.dudupuci.appdespesas.controllers.dtos.request.endereco.AtualizarEnderecoRequestDto;
import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.usuario.AtualizarMeuPerfilRequestDto;
import io.github.dudupuci.appdespesas.models.entities.Assinatura;
import io.github.dudupuci.appdespesas.models.entities.Contato;
import io.github.dudupuci.appdespesas.models.entities.Endereco;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.repositories.UsuariosRepository;
import io.github.dudupuci.appdespesas.services.webservices.AsaasService;
import io.github.dudupuci.appdespesas.services.webservices.dtos.request.CriarCustomerAsaasRequestDto;
import io.github.dudupuci.appdespesas.services.webservices.dtos.response.CustomerCriadoAsaasResponseDto;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    private final UsuariosRepository usuariosRepository;

    @Autowired
    private AsaasService asaasService;

    @Autowired
    private AssinaturaService assinaturaService;

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
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public void assinar(UUID usuarioIdLogado, Long assinaturaId) {
        UsuarioSistema usuario = this.usuariosRepository.findById(usuarioIdLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (AppDespesasUtils.isEntidadeNotNull(usuario.getAssinatura())
                && usuario.getAssinatura().getId().equals(assinaturaId)) {
            throw new RuntimeException("Você já possui essa assinatura");
        }

        Assinatura assinatura = assinaturaService.buscarAssinaturaPorId(assinaturaId);

        if (StringUtils.isEmpty(usuario.getAsaasCustomerId())) {

            if (StringUtils.isEmpty(usuario.getCpfCnpj())) {
                throw new RuntimeException("Para assinar um plano, é necessário informar o CPF/CNPJ no seu perfil.");
            }

           CustomerCriadoAsaasResponseDto customerCriadoDto = asaasService.criarCustomerAsaas(
                   CriarCustomerAsaasRequestDto.fromUsuarioSistema(usuario)
           );

            usuario.setAsaasCustomerId(customerCriadoDto.id());
            this.usuariosRepository.save(usuario);

        }





    }

    // Endpoint admin
    public UsuarioSistema atualizar(UUID usuarioIdLogado, AtualizarUsuarioSistemaRequestDto dto) {
        UsuarioSistema usuarioExistente = this.usuariosRepository.findById(usuarioIdLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Atualiza contato
        Contato contato = usuarioExistente.getContato();
        if (contato == null) contato = new Contato();
        if (dto.contatoDto().celular() != null && !dto.contatoDto().celular().isBlank()) contato.setCelular(dto.contatoDto().celular());
        if (dto.contatoDto().telefoneFixo() != null && !dto.contatoDto().telefoneFixo().isBlank()) contato.setTelefoneFixo(dto.contatoDto().telefoneFixo());
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

    // Endpoint usuario
    public UsuarioSistema atualizar(UUID usuarioIdLogado, AtualizarMeuPerfilRequestDto dto) {
        UsuarioSistema usuarioExistente = this.usuariosRepository.findById(usuarioIdLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Atualiza contato
        Contato contato = usuarioExistente.getContato();
        if (contato == null) contato = new Contato();
        if (dto.contatoDto().celular() != null && !dto.contatoDto().celular().isBlank()) contato.setCelular(dto.contatoDto().celular());
        if (dto.contatoDto().telefoneFixo() != null && !dto.contatoDto().telefoneFixo().isBlank()) contato.setTelefoneFixo(dto.contatoDto().telefoneFixo());
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
