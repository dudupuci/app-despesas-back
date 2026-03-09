package io.github.dudupuci.appdespesas.infrastructure.controllers.users;

import io.github.dudupuci.appdespesas.application.responses.calendario.EventoCalendarioResult;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.responses.calendario.EventoCalendarioResponseDto;
import io.github.dudupuci.appdespesas.application.services.CalendarioService;
import io.github.dudupuci.appdespesas.infrastructure.utils.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/calendarios")
@PreAuthorize("hasRole('USER')")
public class UserCalendarioController {

    private final CalendarioService calendarioService;

    public UserCalendarioController(CalendarioService calendarioService) {
        this.calendarioService = calendarioService;
    }

    @GetMapping
    public ResponseEntity<List<EventoCalendarioResponseDto>> listarItens(
            @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date dataInicio,
            @RequestParam(name = "dataFim", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date dataFim
    ) {
        UUID usuarioId = getUsuarioAutenticadoId();
        List<EventoCalendarioResult> results = calendarioService.buscarEventosCalendario(usuarioId, dataInicio, dataFim);
        List<EventoCalendarioResponseDto> response = results.stream()
                .map(EventoCalendarioResponseDto::fromResult)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    private UUID getUsuarioAutenticadoId() {
        return SecurityUtils.getUsuarioAutenticadoId();
    }
}