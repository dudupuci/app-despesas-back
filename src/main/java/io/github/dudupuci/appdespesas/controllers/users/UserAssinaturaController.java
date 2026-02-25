package io.github.dudupuci.appdespesas.controllers.users;

import io.github.dudupuci.appdespesas.controllers.noauth.dtos.response.assinatura.AssinaturaResponseDto;
import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.assinatura.AssinarAssinaturaRequestDto;
import io.github.dudupuci.appdespesas.controllers.users.dtos.requests.assinatura.CheckoutAssinaturaResponseDto;
import io.github.dudupuci.appdespesas.models.entities.Assinatura;
import io.github.dudupuci.appdespesas.models.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.services.AssinaturaService;
import io.github.dudupuci.appdespesas.services.UsuarioService;
import io.github.dudupuci.appdespesas.services.webservices.dtos.response.ObterQrCodePixResponseDto;
import io.github.dudupuci.appdespesas.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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

    /**
     * Endpoint para preparar a assinatura de um plano
     * <BR/>
     * - O usuário deve fornecer o ID da assinatura que deseja assinar
     * - O sistema irá retornar os detalhes da assinatura selecionada, incluindo o valor e a descrição do plano
     * - O usuário pode revisar os detalhes da assinatura antes de prosseguir para o pagamento
     *
     * @param assinaturaId
     * @return
     */
    @GetMapping("/{assinaturaId}/preparar-assinatura")
    public ResponseEntity<CheckoutAssinaturaResponseDto> prepararAssinatura(
            @PathVariable Long assinaturaId
    ) {
        UUID usuarioIdLogado = getUsuarioAutenticadoId();
        CheckoutAssinaturaResponseDto response = usuarioService.prepararAssinatura(usuarioIdLogado, assinaturaId);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para seguir para pagamento da assinatura
     * <BR/>
     * - O usuário deve fornecer o ID da assinatura que deseja assinar e os detalhes necessários para o pagamento (como forma de pagamento, etc.)
     * - O sistema irá processar a solicitação, criar a assinatura para o usuário e retornar um QR Code Pix para que o usuário possa realizar o pagamento
     * - A assinatura só será ativada após a confirmação do pagamento, que pode ser feita através do QR Code Pix fornecido
     *
     * @param assinaturaRequestDto
     * @param assinaturaId
     * @return
     */
    @PostMapping("/{assinaturaId}/seguir-para-pagamento")
    public ResponseEntity<ObterQrCodePixResponseDto> confirmarAssinatura(
            @Valid @RequestBody AssinarAssinaturaRequestDto assinaturaRequestDto,
            @PathVariable Long assinaturaId
    ) {
        UUID usuarioIdLogado = getUsuarioAutenticadoId();

        ObterQrCodePixResponseDto obterQrCodePixResponse = usuarioService.seguirParaPagamento(
                assinaturaRequestDto,
                usuarioIdLogado,
                assinaturaId
        );

        return ResponseEntity.ok().body(obterQrCodePixResponse);
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
