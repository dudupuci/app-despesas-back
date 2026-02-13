package io.github.dudupuci.appdespesas.controllers;

import io.github.dudupuci.appdespesas.controllers.dtos.calendario.EventoCalendarioDto;
import io.github.dudupuci.appdespesas.services.CalendarioService;
import io.github.dudupuci.appdespesas.utils.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
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
    public ResponseEntity<List<EventoCalendarioDto>> listarItens(
            @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date dataInicio,
            @RequestParam(name = "dataFim", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date dataFim
    ) {
        UUID usuarioId = getUsuarioAutenticadoId();

        List<EventoCalendarioDto> eventos = calendarioService.buscarEventosCalendario(
                usuarioId, dataInicio, dataFim
        );

        return ResponseEntity.ok(eventos);
    }



    private UUID getUsuarioAutenticadoId() {
        return SecurityUtils.getUsuarioAutenticadoId();
    }
}

