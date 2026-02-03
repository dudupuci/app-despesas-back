package io.github.dudupuci.appdespesas.controllers;

import io.github.dudupuci.appdespesas.controllers.dtos.request.categoria.CriarCategoriaRequestDto;
import io.github.dudupuci.appdespesas.controllers.dtos.response.categoria.CategoriaCriadaResponseDto;
import io.github.dudupuci.appdespesas.controllers.dtos.response.categoria.ListCategoriaResponseDto;
import io.github.dudupuci.appdespesas.exceptions.CategoriaJaExisteException;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.services.CategoriasService;
import io.github.dudupuci.appdespesas.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categorias")
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
