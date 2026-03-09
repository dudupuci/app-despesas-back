package io.github.dudupuci.appdespesas.application.usecases;

import io.github.dudupuci.appdespesas.application.commands.assinatura.AssinarAssinaturaCommand;
import io.github.dudupuci.appdespesas.domain.exceptions.*;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.enums.BillingType;
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

    public AssinarPlanoUseCase(
            UsuarioService usuarioService,
            AssinaturaService assinaturaService,
            CobrancaService cobrancaService,
            AsaasService asaasService
    ) {
        this.usuarioService = usuarioService;
        this.assinaturaService = assinaturaService;
        this.cobrancaService = cobrancaService;
        this.asaasService = asaasService;
    }

    public ObterQrCodePixResponseDto executar(
            AssinarAssinaturaCommand cmd,
            UUID usuarioIdLogado,
            Long assinaturaId
    ) {
        if (cmd == null) {
            throw new FormularioNaoPreenchidoException("Dados não preenchidos para realizar assinatura. Preencha o formulário corretamente e tente novamente.");
        }

        UsuarioSistema usuarioLogado = usuarioService.buscarPorId(usuarioIdLogado);
        UsuarioSistema usuarioBeneficiario;

        if (cmd.assinaturaParaOutraPessoa()) {
            usuarioBeneficiario = usuarioService.buscarPorEmail(cmd.email());
            if (usuarioBeneficiario.getId().equals(usuarioLogado.getId())) {
                throw new UsuarioBeneficiarioEqualsUsuarioLogadoException("Desmarque a opção 'Assinar para outra pessoa' ou escolha outro usuário.");
            }
        } else {
            // Validar campos obrigatórios para assinatura própria
            if (StringUtils.isEmpty(cmd.nomeCompleto())) {
                throw new IllegalArgumentException("O nome completo é obrigatório.");
            }
            if (StringUtils.isEmpty(cmd.cpfCnpj())) {
                throw new IllegalArgumentException("O CPF/CNPJ é obrigatório.");
            }
            usuarioBeneficiario = usuarioLogado;
        }

        if (AppDespesasUtils.isEntidadeNotNull(usuarioBeneficiario.getAssinatura())
                && usuarioBeneficiario.getAssinatura().getId().equals(assinaturaId)) {
            throw new UsuarioJaTemEssaAssinaturaException("Este usuário já possui essa assinatura ativa.");
        }

        Assinatura assinatura = assinaturaService.buscarAssinaturaPorId(assinaturaId);

        if (StringUtils.isEmpty(usuarioLogado.getAsaasCustomerId())) {
            if (!cmd.assinaturaParaOutraPessoa()) {
                if (StringUtils.isEmpty(cmd.cpfCnpj())) {
                    throw new CampoObrigatorioException("CPF/CNPJ obrigatório para criar cobrança.");
                }
            } else {
                if (StringUtils.isEmpty(usuarioLogado.getCpfCnpj())) {
                    throw new CampoObrigatorioException("Usuário logado precisa ter CPF/CNPJ cadastrado para realizar pagamento.");
                }
            }

            CustomerCriadoAsaasResponseDto customerCriadoDto =
                    asaasService.criarCustomerAsaas(CriarCustomerAsaasRequestDto.fromUsuarioSistema(usuarioLogado));

            usuarioLogado.setAsaasCustomerId(customerCriadoDto.id());
            usuarioService.atualizar(usuarioLogado);
        }

        BillingType formaPagamento = cmd.formaPagamento();

        CobrancaCriadaAsaasResponseDto cobrancaCriadaDto =
                asaasService.criarCobrancaAsaas(
                        CriarCobrancaAsaasRequestDto.fromObjects(usuarioLogado, assinatura, formaPagamento)
                );

        if (cobrancaCriadaDto.id() != null) {
            Cobranca cobranca = getCobranca(usuarioLogado, assinatura, cobrancaCriadaDto);
            cobrancaService.createOrUpdate(cobranca);
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
