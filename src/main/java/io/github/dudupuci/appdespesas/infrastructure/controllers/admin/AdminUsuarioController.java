package io.github.dudupuci.appdespesas.infrastructure.controllers.admin;

import io.github.dudupuci.appdespesas.infrastructure.controllers.admin.dtos.request.usuarios.AtualizarUsuarioSistemaRequestDto;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/admin/usuarios")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuarioController {

    private final UsuarioService usuarioService;

    public AdminUsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioSistema> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody AtualizarUsuarioSistemaRequestDto dto
    ) {
        UsuarioSistema usuarioAtualizado = usuarioService.atualizar(id, dto);
        return ResponseEntity.ok(usuarioAtualizado);
    }


    @GetMapping
    public ResponseEntity<List<UsuarioSistema>> listarUsuarios() {
        return ResponseEntity.ok(usuarioService.listarUsuarios());
    }
}

