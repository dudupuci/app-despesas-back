package io.github.dudupuci.appdespesas.controllers;

import io.github.dudupuci.appdespesas.controllers.dtos.CriarCategoriaDto;
import io.github.dudupuci.appdespesas.controllers.dtos.response.CategoriaCriadaDto;
import io.github.dudupuci.appdespesas.controllers.dtos.response.ListCategoriaDto;
import io.github.dudupuci.appdespesas.exceptions.CategoriaJaExisteException;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.services.CategoriasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categorias")
@CrossOrigin(value = "http://localhost:3000")
public class CategoriasController {

    @Autowired
    private CategoriasService service;

    @GetMapping
    public ResponseEntity<List<ListCategoriaDto>> listarCategorias() {
        var categorias = service.listarCategoriasBySearch("");
        var dto = categorias.stream().map(ListCategoriaDto::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/pesquisar")
    public ResponseEntity<List<ListCategoriaDto>> listarBySearch(
            @RequestParam(name = "search", required = false) String search
    ) {
        var categorias = service.listarCategoriasBySearch(search);
        var dto = categorias.stream().map(ListCategoriaDto::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CategoriaCriadaDto> create(@RequestBody CriarCategoriaDto dto) {
        try {
            Categoria categoria = service.createCategoria(dto);
            return ResponseEntity.created(URI.create("/categorias/" + categoria.getId()))
                    .body(CategoriaCriadaDto.fromEntityCriada(categoria));
        } catch (CategoriaJaExisteException err) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        this.service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
