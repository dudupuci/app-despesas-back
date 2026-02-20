package io.github.dudupuci.appdespesas.controllers.users;

import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.categoria.CriarCategoriaRequestDto;
import io.github.dudupuci.appdespesas.controllers.users.dtos.responses.categoria.CategoriaCriadaResponseDto;
import io.github.dudupuci.appdespesas.controllers.users.dtos.responses.categoria.ListCategoriaResponseDto;
import io.github.dudupuci.appdespesas.exceptions.CategoriaJaExisteException;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.services.CategoriasService;
import io.github.dudupuci.appdespesas.utils.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Controller para gerenciar categorias de movimentações financeiras (despesas, receitas, etc.)
 * Cada categoria pertence a um usuário específico e pode ser associada a várias movimentações.
 * As categorias ajudam a organizar e classificar as movimentações, permitindo análises e relatórios mais detalhados.
 */

@RestController
@RequestMapping("/categorias")
@PreAuthorize("hasRole('USER')")
public class CategoriasController {

    private final CategoriasService service;

    public CategoriasController(CategoriasService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoriaCriadaResponseDto> create(@RequestBody CriarCategoriaRequestDto dto) {
        try {
            // Obtém o ID do usuário autenticado do token JWT
            UUID usuarioId = getUsuarioAutenticadoId();

            Categoria categoria = service.createCategoria(dto, usuarioId);
            return ResponseEntity.created(URI.create("/categorias/" + categoria.getId()))
                    .body(CategoriaCriadaResponseDto.fromEntityCriada(categoria));
        } catch (CategoriaJaExisteException err) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaCriadaResponseDto> atualizar(
            @PathVariable UUID id,
            @RequestBody CriarCategoriaRequestDto dto
    ) {
        try {
            UUID usuarioId = getUsuarioAutenticadoId();
            Categoria categoria = service.updateCategoria(id, dto, usuarioId);
            return ResponseEntity.ok(CategoriaCriadaResponseDto.fromEntityCriada(categoria));
        } catch (CategoriaJaExisteException err) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        Categoria categoria = service.buscarPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodas(
            @RequestParam(required = false) TipoMovimentacao tipo
    ) {
        UUID usuarioId = getUsuarioAutenticadoId();
        List<Categoria> categorias = service.listarTodasPorUsuarioId(usuarioId, tipo);
        return ResponseEntity.ok(categorias);
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<List<ListCategoriaResponseDto>> listarBySearch(
            @RequestParam(name = "search", required = false) String search
    ) {
        var categorias = service.listarCategoriasBySearch(search);
        var dto = categorias.stream().map(ListCategoriaResponseDto::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        this.service.deletar(id);
        return ResponseEntity.noContent().build();
    }


    private UUID getUsuarioAutenticadoId() {
        return SecurityUtils.getUsuarioAutenticadoId();
    }
}
