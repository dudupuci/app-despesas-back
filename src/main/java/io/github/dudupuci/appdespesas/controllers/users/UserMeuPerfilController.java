package io.github.dudupuci.appdespesas.controllers.users;

import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.usuario.AtualizarMeuPerfilRequestDto;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.services.UsuarioService;
import io.github.dudupuci.appdespesas.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/meu-perfil")
@PreAuthorize("hasAnyRole('USER')")
public class UserMeuPerfilController {

    private final UsuarioService usuarioService;

    public UserMeuPerfilController(
            UsuarioService usuarioService
    ) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<UsuarioSistema> meuPerfil() {
        try {
            UUID usuarioIdLogado = getUsuarioAutenticadoId();
            UsuarioSistema usuario = usuarioService.buscarPorId(usuarioIdLogado);
            return ResponseEntity.ok(usuario);
        } catch (AccessDeniedException err) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping
    public ResponseEntity<UsuarioSistema> atualizarMeuPerfil(
            @Valid @RequestBody AtualizarMeuPerfilRequestDto dto
    ) {
        try {
            UUID usuarioIdLogado = getUsuarioAutenticadoId();
            UsuarioSistema atualizado = usuarioService.atualizar(usuarioIdLogado, dto);
            return ResponseEntity.ok(atualizado);
        } catch (AccessDeniedException err) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    private UUID getUsuarioAutenticadoId() {
        return SecurityUtils.getUsuarioAutenticadoId();
    }
}
