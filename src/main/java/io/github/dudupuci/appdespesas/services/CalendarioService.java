package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.controllers.dtos.calendario.EventoCalendarioDto;
import io.github.dudupuci.appdespesas.models.entities.Compromisso;
import io.github.dudupuci.appdespesas.models.entities.Evento;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.repositories.CompromissoRepository;
import io.github.dudupuci.appdespesas.repositories.EventoRepository;
import io.github.dudupuci.appdespesas.repositories.MovimentacoesRepository;
import io.github.dudupuci.appdespesas.repositories.UsuariosRepository;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service responsável por gerenciar o calendário unificado
 * Agrega Compromissos, Eventos Recorrentes e Movimentações Previstas
 */
@Service
public class CalendarioService {

    private final CompromissoRepository compromissoRepository;
    private final EventoRepository eventoRepository;
    private final MovimentacoesRepository movimentacoesRepository;
    private final UsuariosRepository usuariosRepository;

    public CalendarioService(
            CompromissoRepository compromissoRepository,
            EventoRepository eventoRepository,
            MovimentacoesRepository movimentacoesRepository,
            UsuariosRepository usuariosRepository
    ) {
        this.compromissoRepository = compromissoRepository;
        this.eventoRepository = eventoRepository;
        this.movimentacoesRepository = movimentacoesRepository;
        this.usuariosRepository = usuariosRepository;
    }

    /**
     * Busca todos os eventos do calendário de um usuário em um período
     * @return Lista unificada de todos os tipos de eventos
     */
    public List<EventoCalendarioDto> buscarEventosCalendario(
            UUID usuarioId,
            Date dataInicio,
            Date dataFim
    ) {
        UsuarioSistema usuario = usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<EventoCalendarioDto> eventos = new ArrayList<>();

        // 1. Buscar Compromissos
        List<Compromisso> compromissos = compromissoRepository.findByUsuarioAndPeriodo(
                usuario, dataInicio, dataFim
        );
        compromissos.forEach(c -> eventos.add(
                EventoCalendarioDto.fromCompromisso(
                        c.getId(), c.getTitulo(), c.getDescricao(), c.getDataInicio(),
                        c.getDataFim(), c.getDiaInteiro(), c.getPrioridade(),
                        c.getCor(), c.getConcluido(), c.getLocalizacao()
                )
        ));

        // 2. Buscar Eventos Recorrentes
        List<Evento> eventosRecorrentes = eventoRepository.findByUsuarioAndPeriodo(
                usuario, dataInicio, dataFim
        );
        eventosRecorrentes.forEach(e -> eventos.add(
                EventoCalendarioDto.fromEventoRecorrente(
                        e.getId(), e.getTitulo(), e.getDescricao(), e.getDataInicio(),
                        e.getDataFim(), e.getDiaInteiro(), e.getPrioridade(),
                        e.getCor(), e.getIsRecorrente(),
                        e.getFrequenciaRecorrencia() != null ? e.getFrequenciaRecorrencia().name() : null
                )
        ));

        // 3. Buscar Movimentações Previstas
        List<Movimentacao> movimentacoesPrevistas = movimentacoesRepository.buscarMovimentacoesPrevistas(
                usuarioId, dataInicio, dataFim
        );
        movimentacoesPrevistas.forEach(m -> {
            String cor = m.getTipoMovimentacao().name().equals("DESPESA") ? "#FF5733" : "#28A745";
            eventos.add(
                    EventoCalendarioDto.fromMovimentacaoPrevista(
                            m.getId(), m.getTitulo(), m.getDescricao(),
                            m.getDataDaMovimentacao(), m.getValor(),
                            m.getTipoMovimentacao(), m.getCategoriaNome(),
                            cor, m.getIsRecorrente()
                    )
            );
        });

        // Ordenar todos os eventos por data
        eventos.sort(Comparator.comparing(EventoCalendarioDto::dataInicio));

        return eventos;
    }

    /**
     * Busca eventos de um dia específico
     */
    public List<EventoCalendarioDto> buscarEventosDoDia(UUID usuarioId, Date data) {
        UsuarioSistema usuario = usuariosRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<EventoCalendarioDto> eventos = new ArrayList<>();

        // Compromissos do dia
        compromissoRepository.findByUsuarioAndData(usuario, data)
                .forEach(c -> eventos.add(
                        EventoCalendarioDto.fromCompromisso(
                                c.getId(), c.getTitulo(), c.getDescricao(), c.getDataInicio(),
                                c.getDataFim(), c.getDiaInteiro(), c.getPrioridade(),
                                c.getCor(), c.getConcluido(), c.getLocalizacao()
                        )
                ));

        // Eventos do dia
        eventoRepository.findByUsuarioAndData(usuario, data)
                .forEach(e -> eventos.add(
                        EventoCalendarioDto.fromEventoRecorrente(
                                e.getId(), e.getTitulo(), e.getDescricao(), e.getDataInicio(),
                                e.getDataFim(), e.getDiaInteiro(), e.getPrioridade(),
                                e.getCor(), e.getIsRecorrente(),
                                e.getFrequenciaRecorrencia() != null ? e.getFrequenciaRecorrencia().name() : null
                        )
                ));

        // Movimentações do dia
        movimentacoesRepository.buscarPorData(usuarioId, data)
                .forEach(m -> {
                    String cor = m.getTipoMovimentacao().name().equals("DESPESA") ? "#FF5733" : "#28A745";
                    eventos.add(
                            EventoCalendarioDto.fromMovimentacaoPrevista(
                                    m.getId(), m.getTitulo(), m.getDescricao(),
                                    m.getDataDaMovimentacao(), m.getValor(),
                                    m.getTipoMovimentacao(), m.getCategoriaNome(),
                                    cor, m.getIsRecorrente()
                            )
                    );
                });

        eventos.sort(Comparator.comparing(EventoCalendarioDto::dataInicio));

        return eventos;
    }
}

