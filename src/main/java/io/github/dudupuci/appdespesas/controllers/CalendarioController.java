package io.github.dudupuci.appdespesas.controllers;

import io.github.dudupuci.appdespesas.controllers.dtos.calendario.EventoCalendarioDto;
import io.github.dudupuci.appdespesas.services.CalendarioService;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;
import io.github.dudupuci.appdespesas.utils.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Controller responsável pelos endpoints do calendário unificado
 */
@RestController
@RequestMapping("/calendario")
public class CalendarioController {

    private final CalendarioService calendarioService;

    public CalendarioController(CalendarioService calendarioService) {
        this.calendarioService = calendarioService;
    }

    /**
     * Busca todos os eventos do calendário em um período
     *
     * @param dataInicio Data inicial no formato dd/MM/yyyy
     * @param dataFim Data final no formato dd/MM/yyyy
     * @return Lista unificada de eventos (Compromissos, Eventos, Movimentações)
     */
    @GetMapping
    public ResponseEntity<List<EventoCalendarioDto>> listarEventos(
            @RequestParam String dataInicio,
            @RequestParam String dataFim
    ) {
        UUID usuarioId = getUsuarioAutenticadoId();

        Date inicio = AppDespesasUtils.converterStringParaDate(dataInicio);
        Date fim = AppDespesasUtils.converterStringParaDate(dataFim);

        List<EventoCalendarioDto> eventos = calendarioService.buscarEventosCalendario(
                usuarioId, inicio, fim
        );

        return ResponseEntity.ok(eventos);
    }

    /**
     * Busca eventos de um dia específico
     *
     * @param data Data no formato dd/MM/yyyy
     * @return Lista de eventos do dia
     */
    @GetMapping("/dia")
    public ResponseEntity<List<EventoCalendarioDto>> listarEventosDoDia(
            @RequestParam String data
    ) {
        UUID usuarioId = getUsuarioAutenticadoId();
        Date dataBusca = AppDespesasUtils.converterStringParaDate(data);

        List<EventoCalendarioDto> eventos = calendarioService.buscarEventosDoDia(
                usuarioId, dataBusca
        );

        return ResponseEntity.ok(eventos);
    }


    private UUID getUsuarioAutenticadoId() {
        return SecurityUtils.getUsuarioAutenticadoId();
    }
}

