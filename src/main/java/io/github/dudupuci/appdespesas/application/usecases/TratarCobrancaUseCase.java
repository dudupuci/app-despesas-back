package io.github.dudupuci.appdespesas.application.usecases;

import io.github.dudupuci.appdespesas.application.services.CobrancaService;
import io.github.dudupuci.appdespesas.application.services.NotificacaoService;
import io.github.dudupuci.appdespesas.application.services.validators.NotificacaoValidator;
import io.github.dudupuci.appdespesas.application.services.webservices.EmailService;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.domain.entities.Cobranca;
import io.github.dudupuci.appdespesas.domain.entities.NotificacaoEmail;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.application.services.AssinaturaService;
import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.domain.entities.base.Notificacao;
import io.github.dudupuci.appdespesas.domain.enums.TipoNotificacao;
import org.springframework.stereotype.Service;

@Service
public class TratarCobrancaUseCase {

    private final CobrancaService cobrancaService;
    private final UsuarioService usuarioService;
    private final AssinaturaService assinaturaService;
    private final EmailService emailService;

    public TratarCobrancaUseCase(UsuarioService usuarioService, AssinaturaService assinaturaService, EmailService emailService, CobrancaService cobrancaService) {
        this.usuarioService = usuarioService;
        this.assinaturaService = assinaturaService;
        this.emailService = emailService;
        this.cobrancaService = cobrancaService;
    }

    /**
     * Aqui é onde vou tratar a cobrança confirmada, ou seja,
     * quando o sistema de pagamentos (Asaas) me notificar que uma cobrança foi confirmada,
     * eu vou executar esse método para tratar essa cobrança.
     * @param cobrancaConfirmada
     */
    // Vai precisar mudar futuramente, pois não faz sentido
    // olhar apenas para tipoRecursoPago, pois a cobranca pode ser para Assinatura Ativada, Reativada, etc...
    public void executar(Cobranca cobrancaConfirmada) {
        switch (cobrancaConfirmada.getTipoRecursoPago()) {
            case ASSINATURA -> {
                Assinatura assinatura = assinaturaService.buscarAssinaturaPorId(Long.valueOf(cobrancaConfirmada.getIdRecursoPago()));
                UsuarioSistema usuario = this.usuarioService.buscarPorId(cobrancaConfirmada.getUsuario().getId());

                usuario.setAssinatura(assinatura);
                this.usuarioService.atualizar(usuario);

                Notificacao notificacao = this.emailService.sendEmail(
                        usuario.getContato().getEmail(),
                        "Assinatura ativada com sucesso!",
                                "<p>Olá " + usuario.getNome() + ",</p>" +
                                "<p>Sua assinatura foi ativada com sucesso! " +
                                "Aproveite os benefícios exclusivos que ela oferece.</p>" +
                                "<p>Obrigado por escolher nosso serviço!</p>",
                        usuario.getId(),
                        TipoNotificacao.ASSINATURA_ATIVADA
                );

                // Registro da notificação enviada para o usuário
                // Aqui eu criei um NotificacaoValidator para me ajudar a registrar as notificações enviadas para o usuário,
                // e evitar enviar notificações duplicadas
                NotificacaoValidator notificacaoValidator = new NotificacaoValidator();
                notificacaoValidator.setNotificacoesEnviadasAoUsuario(cobrancaConfirmada.getNotificacoesEnviadasAoUsuario());
                notificacaoValidator.registrarNotificacaoEnviada(notificacao.getTipoNotificacao().getDescricao(), notificacao.getId().toString());
                cobrancaConfirmada.setNotificacoesEnviadasAoUsuario(notificacaoValidator.getNotificacoesEnviadasAoUsuario());

                this.cobrancaService.createOrUpdate(cobrancaConfirmada);

                // Aqui eu poderia atualizar a cobrança no banco de dados
                // Aqui vou criar uma tabela notificacoes para guardar as notificacoes pendentes
                // de serem mostradas/enviadas para o usuário
                // E quando ele abrir o sistema no frontend,
                // vou mostrar as notificações pendentes para ele
                // (como uma notificação de boas-vindas à assinatura,
                // ou um resumo dos benefícios da assinatura, etc)
            }
        }
    }
}
