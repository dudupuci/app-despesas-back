package io.github.dudupuci.appdespesas.controllers;

import io.github.dudupuci.appdespesas.models.entities.Evento;
import io.github.dudupuci.appdespesas.services.EventoService;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;
import io.github.dudupuci.appdespesas.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Controller para gerenciar Eventos (eventos recorrentes, aniversários, etc.)
 */
@RestController
@RequestMapping("/eventos")
public class EventosController {

    private final EventoService eventoService;

    public EventosController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    /**
     * Criar novo evento
     */
    @PostMapping
    public ResponseEntity<Evento> criar(@RequestBody Evento evento) {
        UUID usuarioId = getUsuarioAutenticadoId();
        Evento criado = eventoService.criar(evento, usuarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    /**
     * Buscar evento por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Evento> buscarPorId(@PathVariable Long id) {
        Evento evento = eventoService.buscarPorId(id);
        return ResponseEntity.ok(evento);
    }

    /**
     * Listar todos os eventos do usuário
     */
    @GetMapping
    public ResponseEntity<List<Evento>> listar() {
        UUID usuarioId = getUsuarioAutenticadoId();
        List<Evento> eventos = eventoService.listarTodos(usuarioId);
        return ResponseEntity.ok(eventos);
    }

    /**
     * Listar eventos por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<Evento>> listarPorPeriodo(
            @RequestParam String dataInicio,
            @RequestParam String dataFim
    ) {
        UUID usuarioId = getUsuarioAutenticadoId();
        Date inicio = AppDespesasUtils.converterStringParaDate(dataInicio);
        Date fim = AppDespesasUtils.converterStringParaDate(dataFim);

        List<Evento> eventos = eventoService.listarPorPeriodo(usuarioId, inicio, fim);
        return ResponseEntity.ok(eventos);
    }

    /**
     * Listar apenas eventos recorrentes
     */
    @GetMapping("/recorrentes")
    public ResponseEntity<List<Evento>> listarRecorrentes() {
        UUID usuarioId = getUsuarioAutenticadoId();
        List<Evento> recorrentes = eventoService.listarRecorrentes(usuarioId);
        return ResponseEntity.ok(recorrentes);
    }

    /**
     * Atualizar evento
     */
    @PutMapping("/{id}")
    public ResponseEntity<Evento> atualizar(
            @PathVariable Long id,
            @RequestBody Evento evento
    ) {
        Evento atualizado = eventoService.atualizar(id, evento);
        return ResponseEntity.ok(atualizado);
    }

    /**
     * Deletar evento
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        eventoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private UUID getUsuarioAutenticadoId() {
        return SecurityUtils.getUsuarioAutenticadoId();
    }
}

