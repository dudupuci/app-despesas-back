package io.github.dudupuci.appdespesas.services;

import io.github.dudupuci.appdespesas.controllers.dtos.calendario.EventoCalendarioDto;
import io.github.dudupuci.appdespesas.models.entities.Compromisso;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.repositories.CompromissoRepository;
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
    private final MovimentacoesRepository movimentacoesRepository;
    private final UsuariosRepository usuariosRepository;

    public CalendarioService(
            CompromissoRepository compromissoRepository,
            MovimentacoesRepository movimentacoesRepository,
            UsuariosRepository usuariosRepository
    ) {
        this.compromissoRepository = compromissoRepository;
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
                usuario,
                dataInicio,
                dataFim
        );
        compromissos.forEach(c -> eventos.add(
                EventoCalendarioDto.fromCompromisso(
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

        // 3. Buscar Movimentações Previstas
        List<Movimentacao> movimentacoesPrevistas = movimentacoesRepository.listarPorUsuarioIdEPeriodo(
                usuarioId,
                dataInicio,
                dataFim
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

}

