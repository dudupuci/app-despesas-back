package io.github.dudupuci.appdespesas.controllers.admin;

import io.github.dudupuci.appdespesas.controllers.admin.dtos.request.usuarios.AtualizarUsuarioSistemaRequestDto;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.services.UsuariosService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller para gerenciar usuários do sistema
 * <br/>
 * - Somente usuários com ROLE 'ADMIN' podem acessar os endpoints deste controller
 */
@RestController
@RequestMapping("/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class UsuariosController {

    private final UsuariosService usuariosService;

    public UsuariosController(UsuariosService usuariosService) {
        this.usuariosService = usuariosService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioSistema> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarUsuarioSistemaRequestDto dto
    ) {
        try {
            UsuarioSistema usuarioAtual = usuariosService.buscarPorId(id);
            UsuarioSistema usuarioAtualizado = usuariosService.atualizar(usuarioAtual.getId(), dto);
            return ResponseEntity.ok(usuarioAtualizado);
        } catch (AccessDeniedException err) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @GetMapping
    public ResponseEntity<List<UsuarioSistema>> listarUsuarios() {
        return ResponseEntity.ok(usuariosService.listarUsuarios());
    }
}

