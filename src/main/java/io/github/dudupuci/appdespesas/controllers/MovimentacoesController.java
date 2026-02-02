package io.github.dudupuci.appdespesas.controllers;

import io.github.dudupuci.appdespesas.controllers.dtos.request.movimentacao.CriarMovimentacaoRequestDto;
import io.github.dudupuci.appdespesas.controllers.dtos.response.movimentacao.MovimentacaoCriadaResponseDto;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.services.MovimentacoesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/movimentacoes")
@CrossOrigin(origins = {"https://app-despesas-front.vercel.app", "localhost:3000"})
public class MovimentacoesController {

    @Autowired
    private MovimentacoesService service;

    @PostMapping
    public ResponseEntity<MovimentacaoCriadaResponseDto> create(@RequestBody CriarMovimentacaoRequestDto dto) {
        Movimentacao movimentacao = service.criarMovimentacao(dto);
        MovimentacaoCriadaResponseDto responseDto = MovimentacaoCriadaResponseDto.fromEntityCriada(movimentacao);
        return ResponseEntity.created(URI.create("/movimentacoes/" + movimentacao.getId()))
                .body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        this.service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
