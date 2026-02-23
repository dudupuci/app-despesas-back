package io.github.dudupuci.appdespesas.controllers.users;

import io.github.dudupuci.appdespesas.controllers.users.dtos.responses.calendario.EventoCalendarioResponseDto;
import io.github.dudupuci.appdespesas.services.CalendarioService;
import io.github.dudupuci.appdespesas.utils.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Controller responsável pelos endpoints do calendário unificado
 */
@RestController
@RequestMapping("/calendarios")
@PreAuthorize("hasRole('USER')")
public class UserCalendarioController {

    private final CalendarioService calendarioService;

    public UserCalendarioController(CalendarioService calendarioService) {
        this.calendarioService = calendarioService;
    }

    /**
     * Busca todos os eventos do calendário em um período
     *
     * @param dataInicio Data inicial no formato dd/MM/yyyy
     * @param dataFim Data final no formato dd/MM/yyyy
     * @return Lista unificada de eventos (Compromissos, Eventos, Movimentações)
     */
    @GetMapping("/meu-calendario")
    public ResponseEntity<List<EventoCalendarioResponseDto>> listarItens(
            @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date dataInicio,
            @RequestParam(name = "dataFim", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date dataFim
    ) {
        UUID usuarioId = getUsuarioAutenticadoId();

        List<EventoCalendarioResponseDto> eventos = calendarioService.buscarEventosCalendario(
                usuarioId, dataInicio, dataFim
        );

        return ResponseEntity.ok(eventos);
    }



    private UUID getUsuarioAutenticadoId() {
        return SecurityUtils.getUsuarioAutenticadoId();
    }
}

