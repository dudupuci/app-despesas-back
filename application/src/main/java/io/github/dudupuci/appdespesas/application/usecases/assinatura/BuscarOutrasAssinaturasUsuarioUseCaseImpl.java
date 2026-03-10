package io.github.dudupuci.appdespesas.application.usecases.assinatura;

import io.github.dudupuci.appdespesas.application.services.AssinaturaService;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;

import java.util.List;
import java.util.UUID;

public class BuscarOutrasAssinaturasUsuarioUseCaseImpl extends BuscarOutrasAssinaturasUsuarioUseCase {

    private final AssinaturaService assinaturaService;

    public BuscarOutrasAssinaturasUsuarioUseCaseImpl(AssinaturaService assinaturaService) {
        this.assinaturaService = assinaturaService;
    }

    @Override
    public List<Assinatura> executar(UUID idUsuario) {
        return assinaturaService.buscarOutrasAssinaturasByUsuarioId(idUsuario);
    }
}

