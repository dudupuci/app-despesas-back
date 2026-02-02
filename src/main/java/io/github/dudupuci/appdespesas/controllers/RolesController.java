package io.github.dudupuci.appdespesas.controllers;

import io.github.dudupuci.appdespesas.controllers.dtos.request.role.CriarRoleRequestDto;
import io.github.dudupuci.appdespesas.models.entities.Role;
import io.github.dudupuci.appdespesas.services.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RolesController {

    @Autowired
    private RolesService rolesService;

    @PostMapping
    public ResponseEntity<?> criarRole(@RequestBody CriarRoleRequestDto dto) {
        Role role = this.rolesService.criarRoleIfNaoExistir(dto);
        return ResponseEntity.ok(role);
    }

    @GetMapping
    public ResponseEntity<?> listarRoles() {
        return ResponseEntity.ok(this.rolesService.buscarTodos());
    }
}
