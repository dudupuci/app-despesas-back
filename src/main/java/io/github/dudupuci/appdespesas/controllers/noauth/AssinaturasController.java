package io.github.dudupuci.appdespesas.controllers.noauth;

import io.github.dudupuci.appdespesas.controllers.noauth.dtos.response.assinatura.ListAssinaturaResponseDto;
import io.github.dudupuci.appdespesas.models.entities.Assinatura;
import io.github.dudupuci.appdespesas.services.AssinaturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/assinaturas")
public class AssinaturasController {

    private final AssinaturaService service;

    public AssinaturasController(AssinaturaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ListAssinaturaResponseDto>> listarAssinaturas() {
        List<Assinatura> assinaturas = service.listarAssinaturas();
        List<ListAssinaturaResponseDto> responseDtos = assinaturas
                .stream()
                .map(ListAssinaturaResponseDto::fromEntity)
                .toList();
        return ResponseEntity.ok(responseDtos);
    }

}
