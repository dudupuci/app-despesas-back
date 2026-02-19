package io.github.dudupuci.appdespesas.controllers.users;

import io.github.dudupuci.appdespesas.controllers.dtos.request.usuarios.AtualizarUsuarioSistemaRequestDto;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.services.UsuariosService;
import io.github.dudupuci.appdespesas.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/meu-perfil")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class MeuPerfilController {

    private final UsuariosService usuariosService;

    public MeuPerfilController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @PutMapping
    public ResponseEntity<UsuarioSistema> atualizar(@Valid @RequestBody AtualizarUsuarioSistemaRequestDto dto) {
        try {
            UUID usuarioIdLogado = getUsuarioAutenticadoId();
            UsuarioSistema atualizado = usuariosService.atualizar(usuarioIdLogado, dto);
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
