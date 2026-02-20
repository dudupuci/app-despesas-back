package io.github.dudupuci.appdespesas.controllers.users;

import io.github.dudupuci.appdespesas.controllers.admin.dtos.request.usuarios.AtualizarUsuarioSistemaRequestDto;
import io.github.dudupuci.appdespesas.controllers.noauth.dtos.response.assinatura.AssinaturaResponseDto;
import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.usuario.AtualizarMeuPerfilRequestDto;
import io.github.dudupuci.appdespesas.models.entities.Assinatura;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.services.AssinaturaService;
import io.github.dudupuci.appdespesas.services.UsuariosService;
import io.github.dudupuci.appdespesas.services.annotations.Cep;
import io.github.dudupuci.appdespesas.services.webservices.ViaCepService;
import io.github.dudupuci.appdespesas.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/meu-perfil")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class MeuPerfilController {

    private final UsuariosService usuariosService;
    private final AssinaturaService assinaturaService;

    public MeuPerfilController(
            UsuariosService usuariosService,
            AssinaturaService assinaturaService
    ) {
        this.usuariosService = usuariosService;
        this.assinaturaService = assinaturaService;
    }

    @GetMapping
    public ResponseEntity<UsuarioSistema> meuPerfil() {
        try {
            UUID usuarioIdLogado = getUsuarioAutenticadoId();
            UsuarioSistema usuario = usuariosService.buscarPorId(usuarioIdLogado);
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
            UsuarioSistema atualizado = usuariosService.atualizar(usuarioIdLogado, dto);
            return ResponseEntity.ok(atualizado);
        } catch (AccessDeniedException err) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (Exception err) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @GetMapping("/assinaturas/minha-assinatura")
    public ResponseEntity<AssinaturaResponseDto> listarMinhaAssinatura() {
        UUID usuarioIdLogado = getUsuarioAutenticadoId();
        UsuarioSistema usuario = usuariosService.buscarPorId(usuarioIdLogado);

        Assinatura assinatura = assinaturaService.listarAssinatura(usuario.getAssinatura().getId());
        return ResponseEntity.ok(AssinaturaResponseDto.fromEntity(assinatura));
    }


    @GetMapping("/assinaturas/outras-assinaturas")
    public ResponseEntity<List<AssinaturaResponseDto>> listarOutrasAssinaturas() {
        UUID usuarioIdLogado = getUsuarioAutenticadoId();
        UsuarioSistema usuario = usuariosService.buscarPorId(usuarioIdLogado);

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
