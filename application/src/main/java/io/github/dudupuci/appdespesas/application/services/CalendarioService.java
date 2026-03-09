package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.ports.repositories.CompromissoRepositoryPort;
import io.github.dudupuci.appdespesas.application.ports.repositories.MovimentacaoRepositoryPort;
import io.github.dudupuci.appdespesas.application.responses.calendario.EventoCalendarioResult;
import io.github.dudupuci.appdespesas.domain.entities.Compromisso;
import io.github.dudupuci.appdespesas.domain.entities.Movimentacao;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service responsável por gerenciar o calendário unificado
 * Agrega Compromissos, Eventos Recorrentes e Movimentações Previstas
 */
@Service
public class CalendarioService {

    private final CompromissoRepositoryPort compromissoRepository;
    private final MovimentacaoRepositoryPort movimentacoesRepository;
    private final UsuarioService usuarioService;

    public CalendarioService(
            CompromissoRepositoryPort compromissoRepository,
            MovimentacaoRepositoryPort movimentacoesRepository,
            UsuarioService usuarioService
    ) {
        this.compromissoRepository = compromissoRepository;
        this.movimentacoesRepository = movimentacoesRepository;
        this.usuarioService = usuarioService;
    }

    /**
     * Busca todos os eventos do calendário de um usuário em um período
     * @return Lista unificada de todos os tipos de eventos
     */
    public List<EventoCalendarioResult> buscarEventosCalendario(
            UUID usuarioId,
            Date dataInicio,
            Date dataFim
    ) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        List<EventoCalendarioResult> eventos = new ArrayList<>();

        // 1. Buscar Compromissos
        List<Compromisso> compromissos = compromissoRepository.findByUsuarioAndPeriodo(
                usuario,
                dataInicio,
                dataFim
        );
        compromissos.forEach(c -> eventos.add(
                EventoCalendarioResult.fromCompromisso(
                        c.getId(),
                        c.getTitulo(),
                        c.getDescricao(),
                        c.getDataInicio(),
                        c.getDataFim(),
                        c.getDiaInteiro(),
                        c.getPrioridade(),
                        c.getCor(),
                        c.getConcluido(),
                        c.getLocalizacao()
                )
        ));

        // 2. Buscar Movimentações Previstas
        List<Movimentacao> movimentacoesPrevistas = movimentacoesRepository.listarPorUsuarioIdEPeriodo(
                usuarioId, dataInicio, dataFim
        );
        movimentacoesPrevistas.forEach(m -> {
            String cor = m.getTipoMovimentacao().name().equals("DESPESA") ? "#FF5733" : "#28A745";
            eventos.add(
                    EventoCalendarioResult.fromMovimentacaoPrevista(
                            m.getId(), m.getTitulo(), m.getDescricao(),
                            m.getDataDaMovimentacao(), m.getValor(),
                            m.getTipoMovimentacao(), m.getCategoriaNome(),
                            cor, m.getIsRecorrente()
                    )
            );
        });

        // Ordenar todos os eventos por data
        eventos.sort(Comparator.comparing(EventoCalendarioResult::dataInicio));
        return eventos;
    }

}
