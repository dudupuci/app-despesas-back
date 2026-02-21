package io.github.dudupuci.appdespesas.controllers.users;

import io.github.dudupuci.appdespesas.controllers.noauth.dtos.response.assinatura.AssinaturaResponseDto;
import io.github.dudupuci.appdespesas.models.entities.Assinatura;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.services.AssinaturaService;
import io.github.dudupuci.appdespesas.services.UsuarioService;
import io.github.dudupuci.appdespesas.utils.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/assinaturas")
@PreAuthorize("hasAnyRole('USER')")
public class UserAssinaturaController {

    private final UsuarioService usuarioService;
    private final AssinaturaService assinaturaService;

    public UserAssinaturaController(UsuarioService usuarioService, AssinaturaService assinaturaService) {
        this.usuarioService = usuarioService;
        this.assinaturaService = assinaturaService;
    }

    @PostMapping("/assinar/{id}")
    public ResponseEntity<?> assinar(@PathVariable Long id) {
        UUID usuarioIdLogado = getUsuarioAutenticadoId();

        usuarioService.assinar(usuarioIdLogado, id);
        return ResponseEntity.ok().body("Em breve esta funcionalidade estará disponível!");
    }


    @GetMapping("/minha-assinatura")
    public ResponseEntity<AssinaturaResponseDto> listarMinhaAssinatura() {
        UUID usuarioIdLogado = getUsuarioAutenticadoId();
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioIdLogado);

        Assinatura assinatura = assinaturaService.buscarAssinaturaPorId(usuario.getAssinatura().getId());
        return ResponseEntity.ok(AssinaturaResponseDto.fromEntity(assinatura));
    }


    @GetMapping("/outras-assinaturas")
    public ResponseEntity<List<AssinaturaResponseDto>> listarOutrasAssinaturas() {
        UUID usuarioIdLogado = getUsuarioAutenticadoId();
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioIdLogado);

        List<Assinatura> assinaturas = assinaturaService.listarAssinaturas();
        List<AssinaturaResponseDto> responseDtos = assinaturas
                .stream()
                .filter(a -> !a.getId().equals(usuario.getAssinatura().getId()))
                .map(AssinaturaResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(responseDtos);

    }

    private UUID getUsuarioAutenticadoId() {
        return SecurityUtils.getUsuarioAutenticadoId();
    }
}
