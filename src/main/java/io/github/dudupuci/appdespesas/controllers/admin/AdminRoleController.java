package io.github.dudupuci.appdespesas.controllers.admin;

import io.github.dudupuci.appdespesas.controllers.admin.dtos.request.role.CriarRoleRequestDto;
import io.github.dudupuci.appdespesas.models.entities.Role;
import io.github.dudupuci.appdespesas.services.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/roles")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRoleController {

    private final RoleService roleService;

    public AdminRoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Role> criarRole(@RequestBody CriarRoleRequestDto dto) {
        Role role = this.roleService.criarRoleIfNaoExistir(dto);
        return ResponseEntity.ok(role);
    }

    @GetMapping
    public ResponseEntity<List<Role>> listarRoles() {
        return ResponseEntity.ok(this.roleService.buscarTodos());
    }
}
