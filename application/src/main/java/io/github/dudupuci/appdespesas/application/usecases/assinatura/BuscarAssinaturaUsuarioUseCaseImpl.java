package io.github.dudupuci.appdespesas.application.usecases.assinatura;

import io.github.dudupuci.appdespesas.application.services.AssinaturaService;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;

import java.util.UUID;

public class BuscarAssinaturaUsuarioUseCaseImpl extends BuscarAssinaturaUsuarioUseCase {

    private final AssinaturaService assinaturaService;

    public BuscarAssinaturaUsuarioUseCaseImpl(AssinaturaService assinaturaService) {
        this.assinaturaService = assinaturaService;
    }

    @Override
    public Assinatura executar(UUID idUsuario) {
        return assinaturaService.buscarAssinaturaByUsuarioId(idUsuario);
    }
}

