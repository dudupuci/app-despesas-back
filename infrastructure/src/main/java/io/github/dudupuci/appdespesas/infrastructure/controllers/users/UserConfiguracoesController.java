package io.github.dudupuci.appdespesas.infrastructure.controllers.users;

import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.usuario.configuracoes.EditarConfiguracoesUsuarioRequestDto;
import io.github.dudupuci.appdespesas.application.services.UsuarioConfigService;
import io.github.dudupuci.appdespesas.domain.utils.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/minhas-configuracoes")
@PreAuthorize("hasAnyRole('USER')")
public class UserConfiguracoesController {

    private final UsuarioConfigService usuarioConfigService;

    public UserConfiguracoesController(UsuarioConfigService usuarioConfigService) {
        this.usuarioConfigService = usuarioConfigService;
    }

    @PutMapping
    public ResponseEntity<?> editarConfiguracoes(
            @RequestBody EditarConfiguracoesUsuarioRequestDto editarPreferenciasDto
    ) {
        UUID usuarioIdLogado = getUsuarioAutenticadoId();
        usuarioConfigService.editarConfiguracoes(usuarioIdLogado, editarPreferenciasDto);
        return ResponseEntity.ok().build();
    }

    private UUID getUsuarioAutenticadoId() {
        return SecurityUtils.getUsuarioAutenticadoId();
    }



}
