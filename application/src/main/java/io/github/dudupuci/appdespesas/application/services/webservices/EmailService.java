package io.github.dudupuci.appdespesas.application.services.webservices;


import io.github.dudupuci.appdespesas.application.services.NotificacaoService;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.request.MailtrapEmailRequestDto;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.MailtrapEmailResponseDto;
import io.github.dudupuci.appdespesas.application.services.webservices.feignclients.MailtrapFeignClient;
import io.github.dudupuci.appdespesas.domain.entities.NotificacaoEmail;
import io.github.dudupuci.appdespesas.domain.entities.base.Notificacao;
import io.github.dudupuci.appdespesas.domain.enums.CanalNotificacao;
import io.github.dudupuci.appdespesas.domain.enums.TipoNotificacao;
import io.github.dudupuci.appdespesas.domain.utils.AppDespesasUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmailService {

    private final MailtrapFeignClient mailtrapFeignClient;
    private final NotificacaoService notificacaoService;

    @Value("${integrations.mailtrap.api.token}")
    private String token;

    @Value("${integrations.mailtrap.api.from}")
    private String fromEmail;

    @Value("${integrations.mailtrap.api.name}")
    private String fromName;

    public EmailService(MailtrapFeignClient mailtrapFeignClient, NotificacaoService notificacaoService) {
        this.mailtrapFeignClient = mailtrapFeignClient;
        this.notificacaoService = notificacaoService;
    }

    public Notificacao sendEmail(
            String para,
            String assunto,
            String html,
            UUID usuarioId,
            TipoNotificacao tipoNotificacao
    ) {

        MailtrapEmailRequestDto request = new MailtrapEmailRequestDto(
                new MailtrapEmailRequestDto.Address(fromEmail, fromName),
                List.of(new MailtrapEmailRequestDto.Address(para, null)),
                assunto,
                html
        );


        MailtrapEmailResponseDto responseDto = mailtrapFeignClient.sendEmail(
                "Bearer " + token,
                request
        );

        if (responseDto != null && responseDto.success() &&
                responseDto.message_ids() != null && !responseDto.message_ids().isEmpty()) {

            return notificacaoService.createNotificacao(new NotificacaoEmail(
                            assunto,
                            html,
                            usuarioId,
                            tipoNotificacao,
                            CanalNotificacao.EMAIL
                    )
            );

        } else {
            // Log de falha ou tratamento de erro conforme necessário
            System.err.println("Falha ao enviar email para " + para);
        }

        return null;

    }

}