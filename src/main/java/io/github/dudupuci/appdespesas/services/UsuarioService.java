package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.controllers.admin.dtos.request.usuarios.AtualizarUsuarioSistemaRequestDto;
import io.github.dudupuci.appdespesas.controllers.dtos.request.endereco.AtualizarEnderecoRequestDto;
import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.assinatura.AssinarAssinaturaRequestDto;
import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.assinatura.CheckoutAssinaturaResponseDto;
import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.usuario.AtualizarMeuPerfilRequestDto;
import io.github.dudupuci.appdespesas.exceptions.CpfCnpjObrigatorioException;
import io.github.dudupuci.appdespesas.models.entities.*;
import io.github.dudupuci.appdespesas.models.enums.Status;
import io.github.dudupuci.appdespesas.models.enums.TipoPagamento;
import io.github.dudupuci.appdespesas.models.enums.TipoRecursoPago;
import io.github.dudupuci.appdespesas.repositories.UsuariosRepository;
import io.github.dudupuci.appdespesas.services.webservices.AsaasService;
import io.github.dudupuci.appdespesas.services.webservices.dtos.request.CriarCobrancaAsaasRequestDto;
import io.github.dudupuci.appdespesas.services.webservices.dtos.request.CriarCustomerAsaasRequestDto;
import io.github.dudupuci.appdespesas.services.webservices.dtos.response.CobrancaCriadaAsaasResponseDto;
import io.github.dudupuci.appdespesas.services.webservices.dtos.response.CustomerCriadoAsaasResponseDto;
import io.github.dudupuci.appdespesas.services.webservices.dtos.response.ObterQrCodePixResponseDto;
import io.github.dudupuci.appdespesas.services.webservices.enums.BillingType;
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

    @Autowired
    private CobrancaService cobrancaService;


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


    public CheckoutAssinaturaResponseDto prepararCheckout(
            UUID usuarioIdLogado,
            Long assinaturaId
    ) {

        UsuarioSistema usuario = usuariosRepository.findById(usuarioIdLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Assinatura assinatura = assinaturaService.buscarAssinaturaPorId(assinaturaId);

        if (assinatura == null) {
            throw new RuntimeException("Assinatura não encontrada");
        }

        if (usuario.getAssinatura() != null
                && usuario.getAssinatura().getId().equals(assinaturaId)) {
            throw new RuntimeException("Você já possui esta assinatura.");
        }

        return new CheckoutAssinaturaResponseDto(
                assinatura.getId(),
                assinatura.getNomePlano(),
                assinatura.getValor()
        );
    }

    public ObterQrCodePixResponseDto seguirParaPagamento(
            AssinarAssinaturaRequestDto dto,
            UUID usuarioIdLogado,
            Long assinaturaId
    ) {

        if (dto == null) {
            throw new RuntimeException("Dados da assinatura são obrigatórios");
        }

        UsuarioSistema usuarioLogado = usuariosRepository.findById(usuarioIdLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        UsuarioSistema usuarioBeneficiario;

    /*
        🔎 DEFINE BENEFICIÁRIO
     */
        if (dto.assinaturaParaOutraPessoa()) {

            usuarioBeneficiario = usuariosRepository.buscarPorEmail(dto.email())
                    .orElseThrow(() -> new RuntimeException("Usuário presenteado não encontrado."));

            if (usuarioBeneficiario.getId().equals(usuarioLogado.getId())) {
                throw new RuntimeException("Desmarque a opção 'Assinar para outra pessoa' ou escolha outro usuário.");
            }

        } else {

            dto.validarParaAssinaturaPropria();
            usuarioBeneficiario = usuarioLogado;
        }

    /*
        🔎 VALIDA SE BENEFICIÁRIO JÁ POSSUI ASSINATURA
     */
        if (AppDespesasUtils.isEntidadeNotNull(usuarioBeneficiario.getAssinatura())
                && usuarioBeneficiario.getAssinatura().getId().equals(assinaturaId)) {
            throw new RuntimeException("Este usuário já possui essa assinatura ativa.");
        }

        Assinatura assinatura = assinaturaService.buscarAssinaturaPorId(assinaturaId);

        if (assinatura == null) {
            throw new RuntimeException("Assinatura não encontrada!");
        }

    /*
        💳 PAGADOR É SEMPRE O USUÁRIO LOGADO
     */

        if (StringUtils.isEmpty(usuarioLogado.getAsaasCustomerId())) {

            if (!dto.assinaturaParaOutraPessoa()) {

                // Assinatura própria exige CPF válido
                if (StringUtils.isEmpty(dto.cpfCnpj())) {
                    throw new RuntimeException("CPF/CNPJ obrigatório para criar cobrança.");
                }

            } else {

                // Presente: usar dados já cadastrados do usuário logado
                if (StringUtils.isEmpty(usuarioLogado.getCpfCnpj())) {
                    throw new RuntimeException("Usuário logado precisa ter CPF/CNPJ cadastrado para realizar pagamento.");
                }

            }

            CustomerCriadoAsaasResponseDto customerCriadoDto =
                    asaasService.criarCustomerAsaas(
                            CriarCustomerAsaasRequestDto.fromUsuarioSistema(usuarioLogado)
                    );

            usuarioLogado.setAsaasCustomerId(customerCriadoDto.id());
            usuariosRepository.save(usuarioLogado);
        }

        BillingType formaPagamento = BillingType.PIX;

        Cobranca cobranca = new Cobranca();
        cobranca.setUsuario(usuarioLogado);
        cobranca.setValor(assinatura.getValor());
        cobranca.setStatus(Status.AGUARDANDO_PAGAMENTO);
        cobranca.setMetodo(TipoPagamento.PIX);
        cobranca.setTipoRecursoPago(TipoRecursoPago.ASSINATURA);
        cobranca.setIdRecursoPago(assinatura.getId().toString());
        cobranca.setDataPagamento(null);

        cobrancaService.createCobranca(cobranca);

        CobrancaCriadaAsaasResponseDto cobrancaCriadaDto =
                asaasService.criarCobrancaAsaas(
                        CriarCobrancaAsaasRequestDto.fromObjects(
                                usuarioLogado,
                                assinatura,
                                formaPagamento
                        )
                );

        ObterQrCodePixResponseDto qrCodePix = asaasService.obterQrCodePix(cobrancaCriadaDto.id());

        if (!qrCodePix.success()) {
            throw new RuntimeException("Erro ao gerar QR Code PIX.");
        }

        return new ObterQrCodePixResponseDto(
                true,
                qrCodePix.encodedImage(),
                qrCodePix.payload(),
                qrCodePix.expirationDate(),
                qrCodePix.description(),
                usuarioBeneficiario.getId()
        );
    }

    // Endpoint admin
    public UsuarioSistema atualizar(UUID usuarioIdLogado, AtualizarUsuarioSistemaRequestDto dto) {
        UsuarioSistema usuarioExistente = this.usuariosRepository.findById(usuarioIdLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

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
        UsuarioSistema usuarioExistente = this.usuariosRepository.findById(usuarioIdLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

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


}
