package io.github.dudupuci.appdespesas.infrastructure.controllers.users;

import io.github.dudupuci.appdespesas.application.responses.assinatura.CheckoutAssinaturaResult;
import io.github.dudupuci.appdespesas.infrastructure.controllers.noauth.dtos.response.assinatura.AssinaturaResponseDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.assinatura.AssinarAssinaturaRequestDto;
import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.assinatura.CheckoutAssinaturaResponseDto;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.application.usecases.AssinarPlanoUseCase;
import io.github.dudupuci.appdespesas.application.usecases.BuscarAssinaturaUsuarioUseCase;
import io.github.dudupuci.appdespesas.application.usecases.BuscarOutrasAssinaturasUsuarioUseCase;
import io.github.dudupuci.appdespesas.application.usecases.PrepararAssinaturaPlanoUseCase;
import io.github.dudupuci.appdespesas.application.services.webservices.dtos.response.ObterQrCodePixResponseDto;
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

    private final PrepararAssinaturaPlanoUseCase prepararAssinaturaUseCase;
    private final AssinarPlanoUseCase assinarPlanoUseCase;
    private final BuscarAssinaturaUsuarioUseCase buscarAssinaturaUsuarioIdUseCase;
    private final BuscarOutrasAssinaturasUsuarioUseCase buscarOutrasAssinaturasByUsuarioId;

    public UserAssinaturaController(
            PrepararAssinaturaPlanoUseCase prepararAssinaturaUseCase,
            AssinarPlanoUseCase assinarPlanoUseCase,
            BuscarAssinaturaUsuarioUseCase buscarAssinaturaByUsuarioIdUseCase,
            BuscarAssinaturaUsuarioUseCase buscarAssinaturaUsuarioIdUseCase,
            BuscarOutrasAssinaturasUsuarioUseCase buscarOutrasAssinaturasByUsuarioId
    ) {
        this.prepararAssinaturaUseCase = prepararAssinaturaUseCase;
        this.assinarPlanoUseCase = assinarPlanoUseCase;
        this.buscarAssinaturaUsuarioIdUseCase = buscarAssinaturaUsuarioIdUseCase;
        this.buscarOutrasAssinaturasByUsuarioId = buscarOutrasAssinaturasByUsuarioId;
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
        CheckoutAssinaturaResult result = prepararAssinaturaUseCase.executar(usuarioIdLogado, assinaturaId);
        CheckoutAssinaturaResponseDto response = CheckoutAssinaturaResponseDto.fromResult(result);
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

        ObterQrCodePixResponseDto obterQrCodePixResponse = assinarPlanoUseCase.executar(
                assinaturaRequestDto.toCommand(),
                usuarioIdLogado,
                assinaturaId
        );

        return ResponseEntity.ok().body(obterQrCodePixResponse);
    }


    @GetMapping("/minha-assinatura")
    public ResponseEntity<AssinaturaResponseDto> listarMinhaAssinatura() {
        UUID usuarioIdLogado = getUsuarioAutenticadoId();

        Assinatura assinatura = buscarAssinaturaUsuarioIdUseCase.executar(usuarioIdLogado);
        return ResponseEntity.ok(AssinaturaResponseDto.fromEntity(assinatura));
    }


    @GetMapping("/outras-assinaturas")
    public ResponseEntity<List<AssinaturaResponseDto>> listarOutrasAssinaturas() {
        UUID usuarioIdLogado = getUsuarioAutenticadoId();
        List<Assinatura> outrasAssinaturas = buscarOutrasAssinaturasByUsuarioId.executar(usuarioIdLogado);

        List<AssinaturaResponseDto> responseDtos = outrasAssinaturas
                .stream()
                .map(AssinaturaResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(responseDtos);

    }

    private UUID getUsuarioAutenticadoId() {
        return SecurityUtils.getUsuarioAutenticadoId();
    }
}
