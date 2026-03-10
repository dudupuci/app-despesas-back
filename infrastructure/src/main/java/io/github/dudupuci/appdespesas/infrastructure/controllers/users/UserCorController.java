package io.github.dudupuci.appdespesas.infrastructure.controllers.users;

import io.github.dudupuci.appdespesas.application.services.CorService;
import io.github.dudupuci.appdespesas.application.usecases.cor.AtualizarCorUseCase;
import io.github.dudupuci.appdespesas.application.usecases.base.DeleteCommand;
import io.github.dudupuci.appdespesas.application.usecases.cor.*;
import io.github.dudupuci.appdespesas.application.usecases.cor.CriarCorUseCase;
import io.github.dudupuci.appdespesas.application.usecases.cor.DeletarCorUseCase;
import io.github.dudupuci.appdespesas.domain.entities.Cor;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.cor.CriarCorRequestDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.responses.cor.CorCriadaResponseDto;
import io.github.dudupuci.appdespesas.infrastructure.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cores")
@PreAuthorize("hasRole('USER')")
public class UserCorController {

    private final CriarCorUseCase criarCorUseCase;
    private final AtualizarCorUseCase atualizarCorUseCase;
    private final DeletarCorUseCase deletarCorUseCase;
    private final CorService corService;

    public UserCorController(
            CriarCorUseCase criarCorUseCase,
            AtualizarCorUseCase atualizarCorUseCase,
            DeletarCorUseCase deletarCorUseCase,
            CorService corService
    ) {
        this.criarCorUseCase = criarCorUseCase;
        this.atualizarCorUseCase = atualizarCorUseCase;
        this.deletarCorUseCase = deletarCorUseCase;
        this.corService = corService;
    }

    /**
     * Criar nova cor
     * POST /cores
     */
    @PostMapping
    public ResponseEntity<CorCriadaResponseDto> criar(@Valid @RequestBody CriarCorRequestDto dto) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        Cor criada = criarCorUseCase.executar(dto.toCommand(usuarioId, null));
        return ResponseEntity.created(URI.create("/cores/" + criada.getId()))
                .body(CorCriadaResponseDto.fromEntityCriada(criada));
    }

    /**
     * Listar todas as cores do usuário
     * GET /cores
     */
    @GetMapping
    public ResponseEntity<List<Cor>> listarTodas() {
        return ResponseEntity.ok(corService.listarTodas(SecurityUtils.getUsuarioAutenticadoId()));
    }

    /**
     * Buscar cor por ID
     * GET /cores/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cor> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(corService.buscarPorId(id, SecurityUtils.getUsuarioAutenticadoId()));
    }

    /**
     * Atualizar cor
     * PUT /cores/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cor> atualizar(@PathVariable UUID id, @Valid @RequestBody CriarCorRequestDto dto) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        return ResponseEntity.ok(atualizarCorUseCase.executar(dto.toCommand(usuarioId, id)));
    }

    /**
     * Deletar cor
     * DELETE /cores/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        deletarCorUseCase.executar(new DeleteCommand(usuarioId, id));
        return ResponseEntity.noContent().build();
    }
}
