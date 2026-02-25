package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.controllers.admin.dtos.request.usuarios.AtualizarUsuarioSistemaRequestDto;
import io.github.dudupuci.appdespesas.controllers.dtos.request.endereco.AtualizarEnderecoRequestDto;
import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.assinatura.AssinarAssinaturaRequestDto;
import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.assinatura.CheckoutAssinaturaResponseDto;
import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.usuario.AtualizarMeuPerfilRequestDto;
import io.github.dudupuci.appdespesas.exceptions.*;
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
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public UsuarioSistema buscarPorEmail(String email) {
        return this.usuariosRepository.buscarPorEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuário de email " + email + " não encontrado."));
    }


    public CheckoutAssinaturaResponseDto prepararAssinatura(
            UUID usuarioIdLogado,
            Long assinaturaId
    ) {

        UsuarioSistema usuario = buscarPorId(usuarioIdLogado);

        Assinatura assinatura = assinaturaService.buscarAssinaturaPorId(assinaturaId);
        validarAssinatura(assinatura, usuario);

        return CheckoutAssinaturaResponseDto.fromAssinatura(assinatura);
    }

    public ObterQrCodePixResponseDto seguirParaPagamento(
            AssinarAssinaturaRequestDto dto,
            UUID usuarioIdLogado,
            Long assinaturaId
    ) {

        if (dto == null) {
            throw new FormularioNaoPreenchidoException("Dados não preenchidos para realizar assinatura. Preencha o formulário corretamente e tente novamente.");
        }

        UsuarioSistema usuarioLogado = buscarPorId(usuarioIdLogado);
        UsuarioSistema usuarioBeneficiario;

        // O pagador é sempre o usuário logado, pois é ele quem irá realizar o pagamento da assinatura,
        // ou seja, para si mesmo ou para outra pessoa.

        // Se for assinatura para outra pessoa, o beneficiário é o usuário presenteado
        // Se for assinatura para si mesmo, o beneficiário é o próprio usuário logado
        // Valida se o usuário presenteado existe e não é o mesmo que está assinando

        // Isso evita que um usuário assine para si mesmo usando a opção de "assinar para outra pessoa",
        // o que não faria sentido e poderia causar confusão no sistema
        if (dto.assinaturaParaOutraPessoa()) {
            usuarioBeneficiario = buscarPorEmail(dto.email());

            if (usuarioBeneficiario.getId().equals(usuarioLogado.getId())) {
                throw new UsuarioBeneficiarioEqualsUsuarioLogadoException("Desmarque a opção 'Assinar para outra pessoa' ou escolha outro usuário.");
            }

        } else {
            dto.validarParaAssinaturaPropria();
            usuarioBeneficiario = usuarioLogado;
        }


        // 🔎 VALIDA SE BENEFICIÁRIO JÁ POSSUI ASSINATURA
        if (AppDespesasUtils.isEntidadeNotNull(usuarioBeneficiario.getAssinatura())
                && usuarioBeneficiario.getAssinatura().getId().equals(assinaturaId)) {
            throw new UsuarioJaTemEssaAssinaturaException("Este usuário já possui essa assinatura ativa.");
        }

        Assinatura assinatura = assinaturaService.buscarAssinaturaPorId(assinaturaId);

        // 💳 PAGADOR É SEMPRE O USUÁRIO LOGADO

        // Se o usuário logado não tiver Asaas Customer ID, cria um novo customer no Asaas usando os dados do usuário logado
        // Isso é necessário para que o usuário logado possa ser o pagador da cobrança, mesmo que a assinatura seja para outra pessoa

        // Se o usuário logado já tiver Asaas Customer ID, reutiliza esse ID para criar a cobrança
        // mesmo que a assinatura seja para outra pessoa
        if (StringUtils.isEmpty(usuarioLogado.getAsaasCustomerId())) {

            if (!dto.assinaturaParaOutraPessoa()) {

                // Assinatura própria exige CPF válido
                if (StringUtils.isEmpty(dto.cpfCnpj())) {
                    throw new CampoObrigatorioException("CPF/CNPJ obrigatório para criar cobrança.");
                }

            } else {

                // Presente: usar dados já cadastrados do usuário logado
                if (StringUtils.isEmpty(usuarioLogado.getCpfCnpj())) {
                    throw new CampoObrigatorioException("Usuário logado precisa ter CPF/CNPJ cadastrado para realizar pagamento.");
                }

            }

            // Criar customer no Asaas para o usuário logado
            CustomerCriadoAsaasResponseDto customerCriadoDto =
                    asaasService.criarCustomerAsaas(
                            CriarCustomerAsaasRequestDto.fromUsuarioSistema(usuarioLogado)
                    );

            usuarioLogado.setAsaasCustomerId(customerCriadoDto.id());
            usuariosRepository.save(usuarioLogado);
        }

        BillingType formaPagamento = BillingType.PIX;



        // Criar cobrança no Asaas e obter QR Code Pix
        CobrancaCriadaAsaasResponseDto cobrancaCriadaDto =
                asaasService.criarCobrancaAsaas(
                        CriarCobrancaAsaasRequestDto.fromObjects(
                                usuarioLogado,
                                assinatura,
                                formaPagamento
                        )
                );


        // Criar cobrança no sistema local
        Cobranca cobranca = new Cobranca();
        cobranca.setUsuario(usuarioLogado);
        cobranca.setValor(assinatura.getValor());
        cobranca.setStatus(Status.AGUARDANDO_PAGAMENTO);
        cobranca.setMetodo(TipoPagamento.PIX);
        cobranca.setTipoRecursoPago(TipoRecursoPago.ASSINATURA);
        cobranca.setIdRecursoPago(assinatura.getId().toString());
        cobranca.setIdExterno(cobrancaCriadaDto.id());
        cobranca.setDataPagamento(null);

        cobrancaService.createCobranca(cobranca);

        ObterQrCodePixResponseDto qrCodePix = asaasService.obterQrCodePix(cobrancaCriadaDto.id());

        if (!qrCodePix.success()) {
            throw new ErroAoObterQrCodePixException("Erro ao gerar QR Code PIX.");
        }

        return new ObterQrCodePixResponseDto(
                true,
                qrCodePix.encodedImage(),
                qrCodePix.payload(),
                qrCodePix.expirationDate(),
                qrCodePix.description(),
                usuarioBeneficiario.getId(),
                usuarioBeneficiario.getContato().getEmail()
        );
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

    private void validarAssinatura(Assinatura assinatura, UsuarioSistema usuario) {
        if (usuario.getAssinatura() != null && usuario.getAssinatura().getId().equals(assinatura.getId())) {
            throw new UsuarioJaTemEssaAssinaturaException("Você já possui esta assinatura.");
        }
    }

}
