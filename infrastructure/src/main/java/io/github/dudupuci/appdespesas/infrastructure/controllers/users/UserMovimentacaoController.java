package io.github.dudupuci.appdespesas.infrastructure.controllers.users;

import io.github.dudupuci.appdespesas.application.services.MovimentacaoService;
import io.github.dudupuci.appdespesas.application.usecases.base.DeleteLongCommand;
import io.github.dudupuci.appdespesas.application.usecases.base.DeleteLongCommand;
import io.github.dudupuci.appdespesas.application.usecases.movimentacao.*;
import io.github.dudupuci.appdespesas.domain.entities.Movimentacao;
import io.github.dudupuci.appdespesas.domain.enums.TipoMovimentacao;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.movimentacao.CriarMovimentacaoRequestDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.responses.movimentacao.MovimentacaoCriadaResponseDto;
import io.github.dudupuci.appdespesas.infrastructure.utils.SecurityUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movimentacoes")
@PreAuthorize("hasRole('USER')")
public class UserMovimentacaoController {

    private final CriarMovimentacaoUseCase criarMovimentacaoUseCase;
    private final DeletarMovimentacaoUseCase deletarMovimentacaoUseCase;
    private final MovimentacaoService movimentacaoService;

    public UserMovimentacaoController(CriarMovimentacaoUseCase criarMovimentacaoUseCase,
                                      DeletarMovimentacaoUseCase deletarMovimentacaoUseCase,
                                      MovimentacaoService movimentacaoService) {
        this.criarMovimentacaoUseCase = criarMovimentacaoUseCase;
        this.deletarMovimentacaoUseCase = deletarMovimentacaoUseCase;
        this.movimentacaoService = movimentacaoService;
    }

    @PostMapping
    public ResponseEntity<MovimentacaoCriadaResponseDto> create(@RequestBody CriarMovimentacaoRequestDto dto) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        Movimentacao movimentacao = criarMovimentacaoUseCase.executar(dto.toCommand(usuarioId));
        return ResponseEntity.created(URI.create("/movimentacoes/" + movimentacao.getId()))
                .body(MovimentacaoCriadaResponseDto.fromEntityCriada(movimentacao));
    }

    @GetMapping
    public ResponseEntity<List<Movimentacao>> listarTodas(
            @RequestParam(required = false) TipoMovimentacao tipo,
            @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date dataInicio,
            @RequestParam(name = "dataFim", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date dataFim) {
        return ResponseEntity.ok(movimentacaoService.listarTodasPorUsuarioId(
                SecurityUtils.getUsuarioAutenticadoId(), tipo, dataInicio, dataFim));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movimentacao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(movimentacaoService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        deletarMovimentacaoUseCase.executar(new DeleteLongCommand(usuarioId, id));
        return ResponseEntity.noContent().build();
    }
}
