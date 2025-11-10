package io.github.dudupuci.appdespesas.controllers;

import io.github.dudupuci.appdespesas.controllers.dtos.CriarCategoriaDto;
import io.github.dudupuci.appdespesas.controllers.dtos.response.CategoriaCriadaDto;
import io.github.dudupuci.appdespesas.models.entities.Categoria;
import io.github.dudupuci.appdespesas.services.CategoriasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/categorias")
public class CategoriasController {

    @Autowired
    private CategoriasService service;

    @PostMapping
    public ResponseEntity<CategoriaCriadaDto> createCategoria(@RequestBody CriarCategoriaDto dto) {
        Categoria categoria = service.createCategoria(dto);
        return ResponseEntity.created(URI.create("/categorias/" + categoria.getId()))
                .body(CategoriaCriadaDto.fromCategoria(categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoria(@PathVariable Long id) {
        this.service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
