package io.github.dudupuci.appdespesas.controllers.noauth;

import io.github.dudupuci.appdespesas.controllers.noauth.dtos.response.assinatura.AssinaturaResponseDto;
import io.github.dudupuci.appdespesas.models.entities.Assinatura;
import io.github.dudupuci.appdespesas.services.AssinaturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/assinaturas")
public class NoAuthAssinaturaController {

    private final AssinaturaService service;

    public NoAuthAssinaturaController(AssinaturaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<AssinaturaResponseDto>> listarAssinaturas() {
        List<Assinatura> assinaturas = service.listarAssinaturas();
        List<AssinaturaResponseDto> responseDtos = assinaturas
                .stream()
                .map(AssinaturaResponseDto::fromEntity)
                .toList();

        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssinaturaResponseDto> listarAssinatura(@PathVariable Long id) {
        Assinatura assinatura = service.buscarAssinaturaPorId(id);
        return ResponseEntity.ok(AssinaturaResponseDto.fromEntity(assinatura));
    }

}
