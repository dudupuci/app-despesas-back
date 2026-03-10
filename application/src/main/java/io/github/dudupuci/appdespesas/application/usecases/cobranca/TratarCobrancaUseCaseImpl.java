package io.github.dudupuci.appdespesas.application.usecases.cobranca;

import io.github.dudupuci.appdespesas.application.services.AssinaturaService;
import io.github.dudupuci.appdespesas.application.services.CobrancaService;
import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.application.services.webservices.EmailService;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.entities.base.Notificacao;
import io.github.dudupuci.appdespesas.domain.enums.TipoNotificacao;
import io.github.dudupuci.appdespesas.domain.validators.NotificacaoValidator;

public class TratarCobrancaUseCaseImpl extends TratarCobrancaUseCase {

    private final CobrancaService cobrancaService;
    private final UsuarioService usuarioService;
    private final AssinaturaService assinaturaService;
    private final EmailService emailService;

    public TratarCobrancaUseCaseImpl(CobrancaService cobrancaService, UsuarioService usuarioService,
                                     AssinaturaService assinaturaService, EmailService emailService) {
        this.cobrancaService = cobrancaService;
        this.usuarioService = usuarioService;
        this.assinaturaService = assinaturaService;
        this.emailService = emailService;
    }

    @Override
    public void executar(Cobranca cobrancaConfirmada) {
        switch (cobrancaConfirmada.getTipoRecursoPago()) {
            case ASSINATURA -> {
                Assinatura assinatura = assinaturaService.buscarAssinaturaPorId(Long.valueOf(cobrancaConfirmada.getIdRecursoPago()));
                UsuarioSistema usuario = usuarioService.buscarPorId(cobrancaConfirmada.getUsuario().getId());
                usuario.setAssinatura(assinatura);
                usuarioService.atualizar(usuario);

                Notificacao notificacao = emailService.sendEmail(
                        usuario.getContato().getEmail(),
                        "Assinatura ativada com sucesso!",
                        "<p>Olá " + usuario.getNome() + ",</p>" +
                        "<p>Sua assinatura foi ativada com sucesso! Aproveite os benefícios exclusivos.</p>" +
                        "<p>Obrigado por escolher nosso serviço!</p>",
                        usuario.getId(),
                        TipoNotificacao.ASSINATURA_ATIVADA
                );

                NotificacaoValidator validator = new NotificacaoValidator();
                validator.setNotificacoesEnviadasAoUsuario(cobrancaConfirmada.getNotificacoesEnviadasAoUsuario());
                validator.registrarNotificacaoEnviada(notificacao.getTipoNotificacao().getDescricao(), notificacao.getId().toString());
                cobrancaConfirmada.setNotificacoesEnviadasAoUsuario(validator.getNotificacoesEnviadasAoUsuario());
                cobrancaService.createOrUpdate(cobrancaConfirmada);
            }
        }
    }
}

