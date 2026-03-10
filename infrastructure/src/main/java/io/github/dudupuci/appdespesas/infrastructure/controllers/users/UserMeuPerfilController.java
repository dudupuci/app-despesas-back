package io.github.dudupuci.appdespesas.infrastructure.controllers.users;

import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.application.usecases.usuario.AtualizarPerfilUseCaseImpl;
import io.github.dudupuci.appdespesas.application.usecases.usuario.EditarConfiguracoesUseCaseImpl;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.usuario.AtualizarMeuPerfilRequestDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.usuario.configuracoes.EditarConfiguracoesUsuarioRequestDto;
import io.github.dudupuci.appdespesas.infrastructure.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/meu-perfil")
@PreAuthorize("hasRole('USER')")
public class UserMeuPerfilController {

    private final UsuarioService usuarioService;

    public UserMeuPerfilController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<UsuarioSistema> meuPerfil() {
        return ResponseEntity.ok(usuarioService.buscarPorId(SecurityUtils.getUsuarioAutenticadoId()));
    }

    @PutMapping
    public ResponseEntity<UsuarioSistema> atualizarMeuPerfil(@Valid @RequestBody AtualizarMeuPerfilRequestDto dto) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        UsuarioSistema atualizado = new AtualizarPerfilUseCaseImpl(usuarioService, usuarioId)
                .executar(dto.toCommand());
        return ResponseEntity.ok(atualizado);
    }

    @PatchMapping("/configuracoes")
    public ResponseEntity<Void> editarConfiguracoes(@Valid @RequestBody EditarConfiguracoesUsuarioRequestDto dto) {
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        new EditarConfiguracoesUseCaseImpl(usuarioService, usuarioId).executar(dto.toCommand());
        return ResponseEntity.noContent().build();
    }
}
