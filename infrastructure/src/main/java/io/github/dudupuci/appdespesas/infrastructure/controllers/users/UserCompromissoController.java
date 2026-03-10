package io.github.dudupuci.appdespesas.infrastructure.controllers.users;

import io.github.dudupuci.appdespesas.application.services.CompromissoService;
import io.github.dudupuci.appdespesas.application.usecases.base.DeleteCommand;
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

    private final CriarCompromissoUseCase criarCompromissoUseCase;
    private final AtualizarCompromissoUseCase atualizarCompromissoUseCase;
    private final ConcluirCompromissoUseCase concluirCompromissoUseCase;
    private final DeletarCompromissoUseCase deletarCompromissoUseCase;
    private final CompromissoService compromissoService;

    public UserCompromissoController(CriarCompromissoUseCase criarCompromissoUseCase,
                                     AtualizarCompromissoUseCase atualizarCompromissoUseCase,
                                     ConcluirCompromissoUseCase concluirCompromissoUseCase,
                                     DeletarCompromissoUseCase deletarCompromissoUseCase,
                                     CompromissoService compromissoService) {
        this.criarCompromissoUseCase = criarCompromissoUseCase;
        this.atualizarCompromissoUseCase = atualizarCompromissoUseCase;
        this.concluirCompromissoUseCase = concluirCompromissoUseCase;
        this.deletarCompromissoUseCase = deletarCompromissoUseCase;
        this.compromissoService = compromissoService;
    }

    /**
     * Criar novo compromisso
     */
    @PostMapping
    public ResponseEntity<CompromissoCriadoResponseDto> criar(@Valid @RequestBody CriarCompromissoRequestDto dto) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        Compromisso criado = criarCompromissoUseCase.executar(dto.toCommand(usuarioId, null));
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
    public ResponseEntity<Compromisso> atualizar(@PathVariable UUID id,
                                                 @Valid @RequestBody CriarCompromissoRequestDto dto) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        return ResponseEntity.ok(atualizarCompromissoUseCase.executar(dto.toCommand(usuarioId, id)));
    }

    /**
     * Marcar compromisso como concluído
     */
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Void> concluir(@PathVariable UUID id) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        concluirCompromissoUseCase.executar(new DeleteCommand(usuarioId, id));
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletar compromisso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        deletarCompromissoUseCase.executar(new DeleteCommand(usuarioId, id));
        return ResponseEntity.noContent().build();
    }
}
