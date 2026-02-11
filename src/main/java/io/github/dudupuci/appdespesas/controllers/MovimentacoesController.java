package io.github.dudupuci.appdespesas.controllers;

import io.github.dudupuci.appdespesas.controllers.dtos.request.movimentacao.CriarMovimentacaoRequestDto;
import io.github.dudupuci.appdespesas.controllers.dtos.response.movimentacao.MovimentacaoCriadaResponseDto;
import io.github.dudupuci.appdespesas.models.entities.Movimentacao;
import io.github.dudupuci.appdespesas.models.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.models.enums.TipoPeriodo;
import io.github.dudupuci.appdespesas.services.MovimentacoesService;
import io.github.dudupuci.appdespesas.utils.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacoesController {

    private final MovimentacoesService service;

    public MovimentacoesController(MovimentacoesService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MovimentacaoCriadaResponseDto> create(@RequestBody CriarMovimentacaoRequestDto dto) {
        UUID usuarioId = getUsuarioAutenticadoId();

        Movimentacao movimentacao = service.criarMovimentacao(dto, usuarioId);
        MovimentacaoCriadaResponseDto responseDto = MovimentacaoCriadaResponseDto.fromEntityCriada(movimentacao);
        return ResponseEntity.created(URI.create("/movimentacoes/" + movimentacao.getId()))
                .body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<Movimentacao>> listarTodas(
            @RequestParam(required = false) TipoMovimentacao tipo,
            @RequestParam(required = false) TipoPeriodo tipoPeriodo,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date dataReferencia
    ) {
        UUID usuarioId = getUsuarioAutenticadoId();

        List<Movimentacao> movimentacoes = service.listarTodasPorUsuarioId(
                usuarioId,
                tipo,
                tipoPeriodo,
                dataReferencia
        );
        return ResponseEntity.ok(movimentacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Movimentacao movimentacao = service.buscarPorId(id);
        return ResponseEntity.ok(movimentacao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        this.service.deletar(id);
        return ResponseEntity.noContent().build();
    }


    // Obtém o ID do usuário autenticado do token JWT
    private UUID getUsuarioAutenticadoId() {
        return SecurityUtils.getUsuarioAutenticadoId();
    }
}
