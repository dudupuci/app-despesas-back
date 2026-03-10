package io.github.dudupuci.appdespesas.infrastructure.controllers.users;

import io.github.dudupuci.appdespesas.application.ports.repositories.CategoriaRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CategoriaService;
import io.github.dudupuci.appdespesas.application.services.CorService;
import io.github.dudupuci.appdespesas.application.usecases.categoria.*;
import io.github.dudupuci.appdespesas.domain.entities.Categoria;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.categoria.CriarCategoriaRequestDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.responses.categoria.CategoriaCriadaResponseDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.responses.categoria.ListCategoriaResponseDto;
import io.github.dudupuci.appdespesas.infrastructure.utils.SecurityUtils;
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
public class UserCategoriaController {

    private final CategoriaRepositoryPort categoriaRepository;
    private final CategoriaService categoriaService;
    private final CorService corService;

    public UserCategoriaController(CategoriaRepositoryPort categoriaRepository,
                                   CategoriaService categoriaService,
                                   CorService corService) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaService = categoriaService;
        this.corService = corService;
    }

    @PostMapping
    public ResponseEntity<CategoriaCriadaResponseDto> create(@RequestBody CriarCategoriaRequestDto dto) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        Categoria categoria = new CriarCategoriaUseCaseImpl(categoriaRepository, categoriaService.getUsuarioService(), corService, usuarioId)
                .executar(dto.toCommand());
        return ResponseEntity.created(URI.create("/categorias/" + categoria.getId()))
                .body(CategoriaCriadaResponseDto.fromEntityCriada(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaCriadaResponseDto> atualizar(@PathVariable UUID id, @RequestBody CriarCategoriaRequestDto dto) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        Categoria categoria = new AtualizarCategoriaUseCaseImpl(categoriaRepository, categoriaService, corService, id, usuarioId)
                .executar(dto.toCommand());
        return ResponseEntity.ok(CategoriaCriadaResponseDto.fromEntityCriada(categoria));

    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(categoriaService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodas(@RequestParam(required = false) TipoMovimentacao tipo) {
        return ResponseEntity.ok(categoriaService.listarTodasPorUsuarioId(SecurityUtils.getUsuarioAutenticadoId(), tipo));
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<List<ListCategoriaResponseDto>> listarBySearch(@RequestParam(name = "search", required = false) String search) {
        return ResponseEntity.ok(categoriaService.listarCategoriasBySearch(search)
                .stream().map(ListCategoriaResponseDto::toDto).collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        new DeletarCategoriaUseCaseImpl(categoriaRepository, categoriaService).executar(id);
        return ResponseEntity.noContent().build();
    }
}
