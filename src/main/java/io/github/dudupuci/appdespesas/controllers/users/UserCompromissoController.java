package io.github.dudupuci.appdespesas.controllers.users;

import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.compromisso.CriarCompromissoRequestDto;
import io.github.dudupuci.appdespesas.controllers.users.dtos.responses.compromisso.CompromissoCriadoResponseDto;
import io.github.dudupuci.appdespesas.models.entities.Compromisso;
import io.github.dudupuci.appdespesas.services.CompromissoService;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;
import io.github.dudupuci.appdespesas.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
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
        Compromisso compromisso = dto.toCompromisso();
        Compromisso criado = compromissoService.criar(compromisso, usuarioId);
        return ResponseEntity.created(URI.create("/compromissos/" + criado.getId()))
                .body(CompromissoCriadoResponseDto.fromEntityCriado(criado));
    }

    /**
     * Buscar compromisso por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Compromisso> buscarPorId(@PathVariable Long id) {
        Compromisso compromisso = compromissoService.buscarPorId(id);
        return ResponseEntity.ok(compromisso);
    }

    /**
     * Listar todos os compromissos do usuário
     */
    @GetMapping
    public ResponseEntity<List<Compromisso>> listar() {
        UUID usuarioId = getUsuarioAutenticadoId();
        List<Compromisso> compromissos = compromissoService.listarTodos(usuarioId);
        return ResponseEntity.ok(compromissos);
    }

    /**
     * Listar compromissos por período
     */
    @GetMapping("/periodo")
    public ResponseEntity<List<Compromisso>> listarPorPeriodo(
            @RequestParam String dataInicio,
            @RequestParam String dataFim
    ) {
        UUID usuarioId = getUsuarioAutenticadoId();
        Date inicio = AppDespesasUtils.converterStringParaDate(dataInicio);
        Date fim = AppDespesasUtils.converterStringParaDate(dataFim);

        List<Compromisso> compromissos = compromissoService.listarPorPeriodo(
                usuarioId, inicio, fim
        );
        return ResponseEntity.ok(compromissos);
    }

    /**
     * Listar compromissos pendentes (não concluídos)
     */
    @GetMapping("/pendentes")
    public ResponseEntity<List<Compromisso>> listarPendentes() {
        UUID usuarioId = getUsuarioAutenticadoId();
        List<Compromisso> pendentes = compromissoService.listarPendentes(usuarioId);
        return ResponseEntity.ok(pendentes);
    }

    /**
     * Listar compromissos concluídos
     */
    @GetMapping("/concluidos")
    public ResponseEntity<List<Compromisso>> listarConcluidos() {
        UUID usuarioId = getUsuarioAutenticadoId();
        List<Compromisso> concluidos = compromissoService.listarConcluidos(usuarioId);
        return ResponseEntity.ok(concluidos);
    }

    /**
     * Atualizar compromisso
     */
    @PutMapping("/{id}")
    public ResponseEntity<Compromisso> atualizar(
            @PathVariable Long id,
            @RequestBody Compromisso compromisso
    ) {
        Compromisso atualizado = compromissoService.atualizar(id, compromisso);
        return ResponseEntity.ok(atualizado);
    }

    /**
     * Marcar compromisso como concluído
     */
    @PatchMapping("/{id}/concluir")
    public ResponseEntity<Compromisso> marcarComoConcluido(@PathVariable Long id) {
        Compromisso compromisso = compromissoService.marcarComoConcluido(id);
        return ResponseEntity.ok(compromisso);
    }

    /**
     * Desmarcar conclusão do compromisso
     */
    @PatchMapping("/{id}/desmarcar")
    public ResponseEntity<Compromisso> desmarcarConclusao(@PathVariable Long id) {
        Compromisso compromisso = compromissoService.desmarcarConclusao(id);
        return ResponseEntity.ok(compromisso);
    }

    /**
     * Deletar compromisso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        compromissoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private UUID getUsuarioAutenticadoId() {
        return SecurityUtils.getUsuarioAutenticadoId();
    }
}

