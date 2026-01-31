package io.github.dudupuci.appdespesas.controllers;

import io.github.dudupuci.appdespesas.controllers.dtos.CriarMovimentacaoDto;
import io.github.dudupuci.appdespesas.controllers.dtos.response.MovimentacaoCriadaDto;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.services.MovimentacoesService;
import io.github.dudupuci.appdespesas.utils.AppDespesasUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/movimentacoes")
@CrossOrigin(value = "http://localhost:3000")
public class MovimentacoesController {

    @Autowired
    private MovimentacoesService service;

    @PostMapping
    public ResponseEntity<MovimentacaoCriadaDto> create(@RequestBody CriarMovimentacaoDto dto) {
        Movimentacao movimentacao = service.criarMovimentacao(dto);
        MovimentacaoCriadaDto responseDto = MovimentacaoCriadaDto.fromEntityCriada(movimentacao);
        return ResponseEntity.created(URI.create("/movimentacoes/" + movimentacao.getId()))
                .body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        this.service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
