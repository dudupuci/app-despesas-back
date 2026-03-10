package io.github.dudupuci.appdespesas.infrastructure.controllers.users;

import io.github.dudupuci.appdespesas.application.services.CategoriaService;
import io.github.dudupuci.appdespesas.application.usecases.base.DeleteCommand;
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

@RestController
@RequestMapping("/categorias")
@PreAuthorize("hasRole('USER')")
public class UserCategoriaController {

    private final CriarCategoriaUseCase criarCategoriaUseCase;
    private final AtualizarCategoriaUseCase atualizarCategoriaUseCase;
    private final DeletarCategoriaUseCase deletarCategoriaUseCase;
    private final CategoriaService categoriaService;

    public UserCategoriaController(CriarCategoriaUseCase criarCategoriaUseCase,
                                   AtualizarCategoriaUseCase atualizarCategoriaUseCase,
                                   DeletarCategoriaUseCase deletarCategoriaUseCase,
                                   CategoriaService categoriaService) {
        this.criarCategoriaUseCase = criarCategoriaUseCase;
        this.atualizarCategoriaUseCase = atualizarCategoriaUseCase;
        this.deletarCategoriaUseCase = deletarCategoriaUseCase;
        this.categoriaService = categoriaService;
    }

    @PostMapping
    public ResponseEntity<CategoriaCriadaResponseDto> create(@RequestBody CriarCategoriaRequestDto dto) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        Categoria categoria = criarCategoriaUseCase.executar(dto.toCommand(usuarioId, null));
        return ResponseEntity.created(URI.create("/categorias/" + categoria.getId()))
                .body(CategoriaCriadaResponseDto.fromEntityCriada(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaCriadaResponseDto> atualizar(@PathVariable UUID id,
                                                                @RequestBody CriarCategoriaRequestDto dto) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        Categoria categoria = atualizarCategoriaUseCase.executar(dto.toCommand(usuarioId, id));
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
    public ResponseEntity<List<ListCategoriaResponseDto>> listarBySearch(
            @RequestParam(name = "search", required = false) String search) {
        return ResponseEntity.ok(categoriaService.listarCategoriasBySearch(search)
                .stream().map(ListCategoriaResponseDto::toDto).collect(Collectors.toList()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        deletarCategoriaUseCase.executar(new DeleteCommand(usuarioId, id));
        return ResponseEntity.noContent().build();
    }
}
