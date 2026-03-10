package io.github.dudupuci.appdespesas.application.usecases.assinatura;

import io.github.dudupuci.appdespesas.application.services.AssinaturaService;
import io.github.dudupuci.appdespesas.application.services.CobrancaService;
import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.application.services.webservices.AsaasService;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.CriarCobrancaAsaasRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.CriarCustomerAsaasRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.CobrancaCriadaAsaasResponseDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.CustomerCriadoAsaasResponseDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.ObterQrCodePixResponseDto;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.enums.Status;
import io.github.dudupuci.appdespesas.domain.enums.TipoPagamento;
import io.github.dudupuci.appdespesas.domain.enums.TipoRecursoPago;
import io.github.dudupuci.appdespesas.domain.exceptions.*;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasUtils;
import org.apache.commons.lang3.StringUtils;

public class AssinarPlanoUseCaseImpl extends AssinarPlanoUseCase {

    private final UsuarioService usuarioService;
    private final AssinaturaService assinaturaService;
    private final CobrancaService cobrancaService;
    private final AsaasService asaasService;

    public AssinarPlanoUseCaseImpl(UsuarioService usuarioService, AssinaturaService assinaturaService,
                                   CobrancaService cobrancaService, AsaasService asaasService) {
        this.usuarioService = usuarioService;
        this.assinaturaService = assinaturaService;
        this.cobrancaService = cobrancaService;
        this.asaasService = asaasService;
    }

    @Override
    public ObterQrCodePixResponseDto executar(AssinarAssinaturaCommand cmd) {
        if (cmd == null) throw new FormularioNaoPreenchidoException("Dados não preenchidos para realizar assinatura.");

        UsuarioSistema usuarioLogado = usuarioService.buscarPorId(cmd.usuarioIdLogado());
        UsuarioSistema usuarioBeneficiario;

        if (cmd.assinaturaParaOutraPessoa()) {
            usuarioBeneficiario = usuarioService.buscarPorEmail(cmd.email());
            if (usuarioBeneficiario.getId().equals(usuarioLogado.getId())) {
                throw new UsuarioBeneficiarioEqualsUsuarioLogadoException("Desmarque a opção 'Assinar para outra pessoa' ou escolha outro usuário.");
            }
        } else {
            if (StringUtils.isEmpty(cmd.nomeCompleto())) throw new IllegalArgumentException("O nome completo é obrigatório.");
            if (StringUtils.isEmpty(cmd.cpfCnpj())) throw new IllegalArgumentException("O CPF/CNPJ é obrigatório.");
            usuarioBeneficiario = usuarioLogado;
        }

        if (AppDespesasUtils.isEntidadeNotNull(usuarioBeneficiario.getAssinatura())
                && usuarioBeneficiario.getAssinatura().getId().equals(cmd.assinaturaId())) {
            throw new UsuarioJaTemEssaAssinaturaException("Este usuário já possui essa assinatura ativa.");
        }

        Assinatura assinatura = assinaturaService.buscarAssinaturaPorId(cmd.assinaturaId());

        if (StringUtils.isEmpty(usuarioLogado.getAsaasCustomerId())) {
            if (!cmd.assinaturaParaOutraPessoa() && StringUtils.isEmpty(cmd.cpfCnpj())) {
                throw new CampoObrigatorioException("CPF/CNPJ obrigatório para criar cobrança.");
            }
            if (cmd.assinaturaParaOutraPessoa() && StringUtils.isEmpty(usuarioLogado.getCpfCnpj())) {
                throw new CampoObrigatorioException("Usuário logado precisa ter CPF/CNPJ cadastrado para realizar pagamento.");
            }
            CustomerCriadoAsaasResponseDto customerDto = asaasService.criarCustomerAsaas(
                    CriarCustomerAsaasRequestDto.fromUsuarioSistema(usuarioLogado));
            usuarioLogado.setAsaasCustomerId(customerDto.id());
            usuarioService.atualizar(usuarioLogado);
        }

        CobrancaCriadaAsaasResponseDto cobrancaDto = asaasService.criarCobrancaAsaas(
                CriarCobrancaAsaasRequestDto.fromObjects(usuarioLogado, assinatura, cmd.formaPagamento()));

        if (cobrancaDto.id() == null) throw new ErroAoCriarCobrancaException("Erro ao criar cobrança para assinatura.");

        Cobranca cobranca = new Cobranca();
        cobranca.setUsuario(usuarioLogado);
        cobranca.setValor(assinatura.getValor());
        cobranca.setStatus(Status.AGUARDANDO_PAGAMENTO);
        cobranca.setTipoPagamento(TipoPagamento.PIX);
        cobranca.setTipoRecursoPago(TipoRecursoPago.ASSINATURA);
        cobranca.setIdRecursoPago(assinatura.getId().toString());
        cobranca.setAsaasCobrancaId(cobrancaDto.id());
        cobrancaService.createOrUpdate(cobranca);
        usuarioLogado.adicionaCobranca(cobranca);
        usuarioService.atualizar(usuarioLogado);

        ObterQrCodePixResponseDto qrCode = asaasService.obterQrCodePix(cobrancaDto.id());
        if (!qrCode.success()) throw new ErroAoObterQrCodePixException("Erro ao gerar QR Code PIX.");

        return new ObterQrCodePixResponseDto(true, qrCode.encodedImage(), qrCode.payload(),
                qrCode.expirationDate(), qrCode.description(),
                usuarioBeneficiario.getId(), usuarioBeneficiario.getContato().getEmail());
    }
}
