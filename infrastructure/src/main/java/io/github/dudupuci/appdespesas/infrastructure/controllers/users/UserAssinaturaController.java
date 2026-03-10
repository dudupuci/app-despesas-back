package io.github.dudupuci.appdespesas.infrastructure.controllers.users;

import io.github.dudupuci.appdespesas.application.responses.assinatura.CheckoutAssinaturaResult;
import io.github.dudupuci.appdespesas.application.services.AssinaturaService;
import io.github.dudupuci.appdespesas.application.services.CobrancaService;
import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.application.services.webservices.AsaasService;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.ObterQrCodePixResponseDto;
import io.github.dudupuci.appdespesas.application.usecases.assinatura.*;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.infrastructure.controllers.noauth.dtos.response.assinatura.AssinaturaResponseDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.assinatura.AssinarAssinaturaRequestDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.assinatura.CheckoutAssinaturaResponseDto;
import io.github.dudupuci.appdespesas.infrastructure.utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/assinaturas")
@PreAuthorize("hasAnyRole('USER')")
public class UserAssinaturaController {

    // Injetados pois não têm contexto de requisição
    private final BuscarAssinaturaUsuarioUseCase buscarAssinaturaUsuarioUseCase;
    private final BuscarOutrasAssinaturasUsuarioUseCase buscarOutrasAssinaturasUseCase;

    // Injetados para montar os contextuais nos endpoints
    private final UsuarioService usuarioService;
    private final AssinaturaService assinaturaService;
    private final CobrancaService cobrancaService;
    private final AsaasService asaasService;

    public UserAssinaturaController(
            BuscarAssinaturaUsuarioUseCase buscarAssinaturaUsuarioUseCase,
            BuscarOutrasAssinaturasUsuarioUseCase buscarOutrasAssinaturasUseCase,
            UsuarioService usuarioService,
            AssinaturaService assinaturaService,
            CobrancaService cobrancaService,
            AsaasService asaasService
    ) {
        this.buscarAssinaturaUsuarioUseCase = buscarAssinaturaUsuarioUseCase;
        this.buscarOutrasAssinaturasUseCase = buscarOutrasAssinaturasUseCase;
        this.usuarioService = usuarioService;
        this.assinaturaService = assinaturaService;
        this.cobrancaService = cobrancaService;
        this.asaasService = asaasService;
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
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        CheckoutAssinaturaResult result = new PrepararAssinaturaPlanoUseCaseImpl(usuarioService, assinaturaService, usuarioId)
                .executar(assinaturaId);
        return ResponseEntity.ok(CheckoutAssinaturaResponseDto.fromResult(result));
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
        UUID usuarioId = SecurityUtils.getUsuarioAutenticadoId();
        ObterQrCodePixResponseDto response = new AssinarPlanoUseCaseImpl(
                usuarioService, assinaturaService, cobrancaService, asaasService, usuarioId, assinaturaId)
                .executar(assinaturaRequestDto.toCommand());
        return ResponseEntity.ok(response);
    }


    @GetMapping("/minha-assinatura")
    public ResponseEntity<AssinaturaResponseDto> listarMinhaAssinatura() {
        Assinatura assinatura = buscarAssinaturaUsuarioUseCase.executar(SecurityUtils.getUsuarioAutenticadoId());
        return ResponseEntity.ok(AssinaturaResponseDto.fromEntity(assinatura));
    }


    @GetMapping("/outras-assinaturas")
    public ResponseEntity<List<AssinaturaResponseDto>> listarOutrasAssinaturas() {
        List<Assinatura> outras = buscarOutrasAssinaturasUseCase.executar(SecurityUtils.getUsuarioAutenticadoId());
        return ResponseEntity.ok(outras.stream().map(AssinaturaResponseDto::fromEntity).toList());
    }
}
