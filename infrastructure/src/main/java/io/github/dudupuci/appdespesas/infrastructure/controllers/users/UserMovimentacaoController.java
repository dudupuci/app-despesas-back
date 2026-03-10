package io.github.dudupuci.appdespesas.infrastructure.controllers.users;

import io.github.dudupuci.appdespesas.application.ports.repositories.MovimentacaoRepositoryPort;
import io.github.dudupuci.appdespesas.application.ports.repositories.UsuarioRepositoryPort;
import io.github.dudupuci.appdespesas.application.services.CategoriaService;
import io.github.dudupuci.appdespesas.application.services.MovimentacaoService;
import io.github.dudupuci.appdespesas.application.usecases.movimentacao.CriarMovimentacaoUseCaseImpl;
import io.github.dudupuci.appdespesas.application.usecases.movimentacao.DeletarMovimentacaoUseCaseImpl;
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

    private final MovimentacaoRepositoryPort movimentacaoRepository;
    private final CategoriaService categoriaService;
    private final UsuarioRepositoryPort usuariosRepository;
    private final MovimentacaoService movimentacaoService;

    public UserMovimentacaoController(MovimentacaoRepositoryPort movimentacaoRepository,
                                      CategoriaService categoriaService,
                                      UsuarioRepositoryPort usuariosRepository,
                                      MovimentacaoService movimentacaoService) {
        this.movimentacaoRepository = movimentacaoRepository;
        this.categoriaService = categoriaService;
        this.usuariosRepository = usuariosRepository;
        this.movimentacaoService = movimentacaoService;
    }

    @PostMapping
    public ResponseEntity<MovimentacaoCriadaResponseDto> create(@RequestBody CriarMovimentacaoRequestDto dto) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        Movimentacao movimentacao = new CriarMovimentacaoUseCaseImpl(movimentacaoRepository, categoriaService, usuariosRepository, usuarioId)
                .executar(dto.toCommand());
        return ResponseEntity.created(URI.create("/movimentacoes/" + movimentacao.getId()))
                .body(MovimentacaoCriadaResponseDto.fromEntityCriada(movimentacao));
    }

    @GetMapping
    public ResponseEntity<List<Movimentacao>> listarTodas(
            @RequestParam(required = false) TipoMovimentacao tipo,
            @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date dataInicio,
            @RequestParam(name = "dataFim", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") Date dataFim) {
        return ResponseEntity.ok(movimentacaoService.listarTodasPorUsuarioId(SecurityUtils.getUsuarioAutenticadoId(), tipo, dataInicio, dataFim));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movimentacao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(movimentacaoService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        new DeletarMovimentacaoUseCaseImpl(movimentacaoRepository, movimentacaoService).executar(id);
        return ResponseEntity.noContent().build();
    }
}
