package io.github.dudupuci.appdespesas.infrastructure.controllers.users;

import io.github.dudupuci.appdespesas.application.ports.repositories.CompromissoRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CompromissoService;
import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.application.usecases.compromisso.*;
import io.github.dudupuci.appdespesas.domain.entities.Compromisso;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.compromisso.CriarCompromissoRequestDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.responses.compromisso.CompromissoCriadoResponseDto;
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

    private final CompromissoRepositoryPort compromissoRepository;
    private final CompromissoService compromissoService;
    private final UsuarioService usuarioService;

    public UserCompromissoController(CompromissoRepositoryPort compromissoRepository,
                                     CompromissoService compromissoService,
                                     UsuarioService usuarioService) {
        this.compromissoRepository = compromissoRepository;
        this.compromissoService = compromissoService;
        this.usuarioService = usuarioService;
    }

    /**
     * Criar novo compromisso
     */
    @PostMapping
    public ResponseEntity<CompromissoCriadoResponseDto> criar(@Valid @RequestBody CriarCompromissoRequestDto dto) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        Compromisso criado = new CriarCompromissoUseCaseImpl(compromissoRepository, usuarioService, usuarioId)
                .executar(dto.toCommand());
        return ResponseEntity.created(URI.create("/compromissos/" + criado.getId()))
                .body(CompromissoCriadoResponseDto.fromEntityCriado(criado));
    }

    /**
     * Buscar compromisso por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Compromisso> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(compromissoService.buscarPorId(id));
    }

    /**
     * Listar todos os compromissos do usuário
     */
    @GetMapping
    public ResponseEntity<List<Compromisso>> listar() {
        return ResponseEntity.ok(compromissoService.listarTodos(SecurityUtils.getUsuarioAutenticadoId()));
    }

    /**
     * Listar compromissos pendentes (não concluídos)
     */
    @GetMapping("/pendentes")
    public ResponseEntity<List<Compromisso>> listarPendentes() {
        return ResponseEntity.ok(compromissoService.listarPendentes(SecurityUtils.getUsuarioAutenticadoId()));
    }

    /**
     * Listar compromissos concluídos
     */
    @GetMapping("/concluidos")
    public ResponseEntity<List<Compromisso>> listarConcluidos() {
        return ResponseEntity.ok(compromissoService.listarConcluidos(SecurityUtils.getUsuarioAutenticadoId()));
    }

    /**
     * Atualizar compromisso
     */
    @PutMapping("/{id}")
    public ResponseEntity<Compromisso> atualizar(@PathVariable UUID id, @Valid @RequestBody CriarCompromissoRequestDto dto) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        return ResponseEntity.ok(new AtualizarCompromissoUseCaseImpl(compromissoRepository, compromissoService, id, usuarioId)
                .executar(dto.toCommand()));
    }

    /**
     * Marcar compromisso como concluído
     */
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Void> concluir(@PathVariable UUID id) {
        new ConcluirCompromissoUseCaseImpl(compromissoRepository, compromissoService, SecurityUtils.getUsuarioAutenticadoId())
                .executar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletar compromisso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        new DeletarCompromissoUseCaseImpl(compromissoRepository, compromissoService, SecurityUtils.getUsuarioAutenticadoId())
                .executar(id);
        return ResponseEntity.noContent().build();
    }
}
