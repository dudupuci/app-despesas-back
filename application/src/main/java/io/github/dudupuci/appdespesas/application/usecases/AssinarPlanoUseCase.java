package io.github.dudupuci.appdespesas.application.usecases;

import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.assinatura.AssinarAssinaturaRequestDto;
import io.github.dudupuci.appdespesas.domain.exceptions.*;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.enums.Status;
import io.github.dudupuci.appdespesas.domain.enums.TipoPagamento;
import io.github.dudupuci.appdespesas.domain.enums.TipoRecursoPago;
import io.github.dudupuci.appdespesas.application.services.AssinaturaService;
import io.github.dudupuci.appdespesas.application.services.CobrancaService;
import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.application.services.webservices.AsaasService;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.CriarCobrancaAsaasRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.CriarCustomerAsaasRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.CobrancaCriadaAsaasResponseDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.CustomerCriadoAsaasResponseDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.ObterQrCodePixResponseDto;
import io.github.dudupuci.appdespesas.domain.enums.BillingType;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AssinarPlanoUseCase {

    private final UsuarioService usuarioService;
    private final AssinaturaService assinaturaService;
    private final CobrancaService cobrancaService;
    private final AsaasService asaasService;

    public AssinarPlanoUseCase(UsuarioService usuarioService, AssinaturaService assinaturaService, CobrancaService cobrancaService, AsaasService asaasService) {
        this.usuarioService = usuarioService;
        this.assinaturaService = assinaturaService;
        this.cobrancaService = cobrancaService;
        this.asaasService = asaasService;
    }

    public ObterQrCodePixResponseDto executar(
            AssinarAssinaturaRequestDto dto,
            UUID usuarioIdLogado,
            Long assinaturaId
    ) {

        if (dto == null) {
            throw new FormularioNaoPreenchidoException("Dados não preenchidos para realizar assinatura. Preencha o formulário corretamente e tente novamente.");
        }

        UsuarioSistema usuarioLogado = usuarioService.buscarPorId(usuarioIdLogado);
        UsuarioSistema usuarioBeneficiario;

        // O pagador é sempre o usuário logado, pois é ele quem irá realizar o pagamento da assinatura,
        // ou seja, para si mesmo ou para outra pessoa.

        // Se for assinatura para outra pessoa, o beneficiário é o usuário presenteado
        // Se for assinatura para si mesmo, o beneficiário é o próprio usuário logado
        // Valida se o usuário presenteado existe e não é o mesmo que está assinando

        // Isso evita que um usuário assine para si mesmo usando a opção de "assinar para outra pessoa",
        // o que não faria sentido e poderia causar confusão no sistema
        if (dto.assinaturaParaOutraPessoa()) {
            usuarioBeneficiario = usuarioService.buscarPorEmail(dto.email());

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
            usuarioService.atualizar(usuarioLogado);
        }

        BillingType formaPagamento = dto.formaPagamento();

        // Criar cobrança no Asaas e obter QR Code Pix
        CobrancaCriadaAsaasResponseDto cobrancaCriadaDto =
                asaasService.criarCobrancaAsaas(
                        CriarCobrancaAsaasRequestDto.fromObjects(
                                usuarioLogado,
                                assinatura,
                                formaPagamento
                        )
                );


        if (cobrancaCriadaDto.id() != null) {

            // Criar cobrança no sistema local
            Cobranca cobranca = getCobranca(usuarioLogado, assinatura, cobrancaCriadaDto);
            cobrancaService.createCobranca(cobranca);

            // Associar cobrança ao usuário logado (pagador)
            usuarioLogado.adicionaCobranca(cobranca);
            usuarioService.atualizar(usuarioLogado);

        } else {
            throw new ErroAoCriarCobrancaException("Erro ao criar cobrança para assinatura. Tente novamente.");
        }


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

    private static Cobranca getCobranca(
            UsuarioSistema usuarioLogado,
            Assinatura assinatura,
            CobrancaCriadaAsaasResponseDto cobrancaCriadaDto
    ) {
        Cobranca cobranca = new Cobranca();
        cobranca.setUsuario(usuarioLogado);
        cobranca.setValor(assinatura.getValor());
        cobranca.setStatus(Status.AGUARDANDO_PAGAMENTO);
        cobranca.setTipoPagamento(TipoPagamento.PIX);
        cobranca.setTipoRecursoPago(TipoRecursoPago.ASSINATURA);
        cobranca.setIdRecursoPago(assinatura.getId().toString());
        cobranca.setAsaasCobrancaId(cobrancaCriadaDto.id());
        cobranca.setDataPagamento(null);
        return cobranca;
    }
}
