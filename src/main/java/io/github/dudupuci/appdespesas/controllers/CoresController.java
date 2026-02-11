package io.github.dudupuci.appdespesas.controllers;

import io.github.dudupuci.appdespesas.controllers.dtos.request.cor.CriarCorRequestDto;
import io.github.dudupuci.appdespesas.controllers.dtos.response.cor.CorCriadaResponseDto;
import io.github.dudupuci.appdespesas.models.entities.Cor;
import io.github.dudupuci.appdespesas.services.CorService;
import io.github.dudupuci.appdespesas.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cores")
public class CoresController {

    private final CorService corService;

    public CoresController(CorService corService) {
        this.corService = corService;
    }

    /**
     * Criar nova cor
     * POST /cores
     */
    @PostMapping
    public ResponseEntity<CorCriadaResponseDto> criar(@Valid @RequestBody CriarCorRequestDto dto) {
        UUID usuarioId = getUsuarioAutenticadoId();
        Cor cor = dto.toCor();
        Cor criada = corService.criar(cor, usuarioId);
        return ResponseEntity.created(URI.create("/cores/" + criada.getId()))
                .body(CorCriadaResponseDto.fromEntityCriada(criada));
    }

    /**
     * Listar todas as cores do usuário
     * GET /cores
     */
    @GetMapping
    public ResponseEntity<List<Cor>> listarTodas() {
        UUID usuarioId = getUsuarioAutenticadoId();
        List<Cor> cores = corService.listarTodas(usuarioId);
        return ResponseEntity.ok(cores);
    }

    /**
     * Buscar cor por ID
     * GET /cores/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cor> buscarPorId(@PathVariable UUID id) {
        UUID usuarioId = getUsuarioAutenticadoId();
        Cor cor = corService.buscarPorId(id, usuarioId);
        return ResponseEntity.ok(cor);
    }

    /**
     * Pesquisar cores por nome
     * GET /cores/pesquisar?nome=vermelho
     */
    @GetMapping("/pesquisar")
    public ResponseEntity<List<Cor>> pesquisar(@RequestParam String nome) {
        UUID usuarioId = getUsuarioAutenticadoId();
        List<Cor> cores = corService.pesquisarPorNome(nome, usuarioId);
        return ResponseEntity.ok(cores);
    }

    /**
     * Atualizar cor
     * PUT /cores/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Cor> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody CriarCorRequestDto dto
    ) {
        UUID usuarioId = getUsuarioAutenticadoId();
        Cor corAtualizada = dto.toCor();
        Cor cor = corService.atualizar(id, corAtualizada, usuarioId);
        return ResponseEntity.ok(cor);
    }

    /**
     * Deletar cor
     * DELETE /cores/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        UUID usuarioId = getUsuarioAutenticadoId();
        corService.deletar(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    private UUID getUsuarioAutenticadoId() {
        return SecurityUtils.getUsuarioAutenticadoId();
    }
}

