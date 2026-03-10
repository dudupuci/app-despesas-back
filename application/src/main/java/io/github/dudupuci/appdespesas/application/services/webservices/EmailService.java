package io.github.dudupuci.appdespesas.application.services.webservices;


import io.github.dudupuci.appdespesas.application.ports.services.MailtrapPort;
import io.github.dudupuci.appdespesas.application.services.NotificacaoService;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.MailtrapEmailRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.MailtrapEmailResponseDto;
import io.github.dudupuci.appdespesas.domain.entities.NotificacaoEmail;
import io.github.dudupuci.appdespesas.domain.entities.base.Notificacao;
import io.github.dudupuci.appdespesas.domain.enums.CanalNotificacao;
import io.github.dudupuci.appdespesas.domain.enums.TipoNotificacao;

import java.util.List;
import java.util.UUID;

public class EmailService {

    private final MailtrapPort mailtrapPort;
    private final NotificacaoService notificacaoService;
    private final String token;
    private final String fromEmail;
    private final String fromName;

    public EmailService(MailtrapPort mailtrapPort, NotificacaoService notificacaoService,
                        String token, String fromEmail, String fromName) {
        this.mailtrapPort = mailtrapPort;
        this.notificacaoService = notificacaoService;
        this.token = token;
        this.fromEmail = fromEmail;
        this.fromName = fromName;
    }

    public Notificacao sendEmail(String para, String assunto, String html,
                                 UUID usuarioId, TipoNotificacao tipoNotificacao) {
        MailtrapEmailRequestDto request = new MailtrapEmailRequestDto(
                new MailtrapEmailRequestDto.Address(fromEmail, fromName),
                List.of(new MailtrapEmailRequestDto.Address(para, null)),
                assunto,
                html
        );

        MailtrapEmailResponseDto responseDto = mailtrapPort.sendEmail("Bearer " + token, request);

        if (responseDto != null && responseDto.success() &&
                responseDto.message_ids() != null && !responseDto.message_ids().isEmpty()) {
            return notificacaoService.createNotificacao(new NotificacaoEmail(
                    assunto, html, usuarioId, tipoNotificacao, CanalNotificacao.EMAIL));
        } else {
            System.err.println("Falha ao enviar email para " + para);
        }
        return null;
    }

}