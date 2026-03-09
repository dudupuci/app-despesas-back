package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.domain.entities.base.Notificacao;
import io.github.dudupuci.appdespesas.infrastructure.repositories.NotificacaoRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    private final NotificacaoRepository notificacaoRepository;

    public NotificacaoService(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    public Notificacao createNotificacao(Notificacao notificacao) {
        return this.notificacaoRepository.save(notificacao);
    }

}
