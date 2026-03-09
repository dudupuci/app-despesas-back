package io.github.dudupuci.appdespesas.infrastructure.controllers.users;

import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.compromisso.CriarCompromissoRequestDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.responses.compromisso.CompromissoCriadoResponseDto;
import io.github.dudupuci.appdespesas.domain.entities.Compromisso;
import io.github.dudupuci.appdespesas.application.services.CompromissoService;
import io.github.dudupuci.appdespesas.infrastructure.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * Controller para gerenciar Compromissos (tarefas, afazeres, reuniões, etc.)
 */
@RestController
@RequestMapping("/compromissos")
@PreAuthorize("hasRole('USER')")
public class UserCompromissoController {

    private final CompromissoService compromissoService;

    public UserCompromissoController(CompromissoService compromissoService) {
        this.compromissoService = compromissoService;
    }

    /**
     * Criar novo compromisso
     */
    @PostMapping
    public ResponseEntity<CompromissoCriadoResponseDto> criar(@Valid @RequestBody CriarCompromissoRequestDto dto) {
        UUID usuarioId = getUsuarioAutenticadoId();
        Compromisso criado = compromissoService.criar(dto.toCommand(), usuarioId);
        return ResponseEntity.created(URI.create("/compromissos/" + criado.getId()))
                .body(CompromissoCriadoResponseDto.fromEntityCriado(criado));
    }

    /**
     * Buscar compromisso por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Compromisso> buscarPorId(@PathVariable UUID id) {
        Compromisso compromisso = compromissoService.buscarPorId(id);
        return ResponseEntity.ok(compromisso);
    }

    /**
     * Listar todos os compromissos do usuário
     */
    @GetMapping
    public ResponseEntity<List<Compromisso>> listar() {
        UUID usuarioId = getUsuarioAutenticadoId();
        return ResponseEntity.ok(compromissoService.listarTodos(usuarioId));
    }

    /**
     * Listar compromissos pendentes (não concluídos)
     */
    @GetMapping("/pendentes")
    public ResponseEntity<List<Compromisso>> listarPendentes() {
        UUID usuarioId = getUsuarioAutenticadoId();
        return ResponseEntity.ok(compromissoService.listarPendentes(usuarioId));
    }

    /**
     * Listar compromissos concluídos
     */
    @GetMapping("/concluidos")
    public ResponseEntity<List<Compromisso>> listarConcluidos() {
        UUID usuarioId = getUsuarioAutenticadoId();
        return ResponseEntity.ok(compromissoService.listarConcluidos(usuarioId));
    }

    /**
     * Atualizar compromisso
     */
    @PutMapping("/{id}")
    public ResponseEntity<Compromisso> atualizar(@PathVariable UUID id, @Valid @RequestBody CriarCompromissoRequestDto dto) {
        UUID usuarioId = getUsuarioAutenticadoId();
        return ResponseEntity.ok(compromissoService.atualizar(id, dto.toCommand(), usuarioId));
    }

    /**
     * Marcar compromisso como concluído
     */
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Void> concluir(@PathVariable UUID id) {
        UUID usuarioId = getUsuarioAutenticadoId();
        compromissoService.concluir(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletar compromisso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        UUID usuarioId = getUsuarioAutenticadoId();
        compromissoService.deletar(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    private UUID getUsuarioAutenticadoId() {
        return SecurityUtils.getUsuarioAutenticadoId();
    }
}
