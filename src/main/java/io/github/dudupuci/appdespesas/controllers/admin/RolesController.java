package io.github.dudupuci.appdespesas.controllers.admin;

import io.github.dudupuci.appdespesas.controllers.admin.dtos.request.role.CriarRoleRequestDto;
import io.github.dudupuci.appdespesas.models.entities.Role;
import io.github.dudupuci.appdespesas.services.RolesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RolesController {

    private final RolesService rolesService;

    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @PostMapping
    public ResponseEntity<Role> criarRole(@RequestBody CriarRoleRequestDto dto) {
        Role role = this.rolesService.criarRoleIfNaoExistir(dto);
        return ResponseEntity.ok(role);
    }

    @GetMapping
    public ResponseEntity<List<Role>> listarRoles() {
        return ResponseEntity.ok(this.rolesService.buscarTodos());
    }
}
