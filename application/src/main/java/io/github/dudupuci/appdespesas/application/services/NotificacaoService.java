package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.ports.repositories.NotificacaoRepositoryPort;
import io.github.dudupuci.appdespesas.domain.entities.base.Notificacao;
import org.springframework.stereotype.Service;

@Service
public class NotificacaoService {

    private final NotificacaoRepositoryPort notificacaoRepository;

    public NotificacaoService(NotificacaoRepositoryPort notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    public Notificacao createNotificacao(Notificacao notificacao) {
        return this.notificacaoRepository.save(notificacao);
    }
}
