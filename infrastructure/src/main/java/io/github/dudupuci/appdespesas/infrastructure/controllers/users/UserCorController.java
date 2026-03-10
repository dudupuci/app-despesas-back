package io.github.dudupuci.appdespesas.infrastructure.controllers.users;

import io.github.dudupuci.appdespesas.application.ports.repositories.CorRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CorService;
import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.application.usecases.cor.*;
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

    private final CorRepositoryPort corRepository;
    private final CorService corService;
    private final UsuarioService usuarioService;

    public UserCorController(CorRepositoryPort corRepository, CorService corService, UsuarioService usuarioService) {
        this.corRepository = corRepository;
        this.corService = corService;
        this.usuarioService = usuarioService;
    }

    /**
     * Criar nova cor
     * POST /cores
     */
    @PostMapping
    public ResponseEntity<CorCriadaResponseDto> criar(@Valid @RequestBody CriarCorRequestDto dto) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        Cor criada = new CriarCorUseCaseImpl(corRepository, usuarioService, usuarioId).executar(dto.toCommand());
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
        return ResponseEntity.ok(new AtualizarCorUseCaseImpl(corRepository, corService, usuarioService, id, usuarioId).executar(dto.toCommand()));
    }

    /**
     * Deletar cor
     * DELETE /cores/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        new DeletarCorUseCaseImpl(corRepository, corService, SecurityUtils.getUsuarioAutenticadoId()).executar(id);
        return ResponseEntity.noContent().build();
    }
}
