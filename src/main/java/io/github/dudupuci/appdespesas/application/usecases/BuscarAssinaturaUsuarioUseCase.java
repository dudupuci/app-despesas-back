package io.github.dudupuci.appdespesas.application.usecases;

import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.application.services.AssinaturaService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BuscarAssinaturaUsuarioUseCase {

    private final AssinaturaService assinaturaService;

    public BuscarAssinaturaUsuarioUseCase(AssinaturaService assinaturaService) {
        this.assinaturaService = assinaturaService;
    }

    public Assinatura executar(UUID idUsuario) {
        return assinaturaService.buscarAssinaturaByUsuarioId(idUsuario);

    }
}
