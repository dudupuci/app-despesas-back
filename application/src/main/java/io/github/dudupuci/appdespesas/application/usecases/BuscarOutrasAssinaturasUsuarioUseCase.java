package io.github.dudupuci.appdespesas.application.usecases;

import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.application.services.AssinaturaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BuscarOutrasAssinaturasUsuarioUseCase {

    private final AssinaturaService assinaturaService;

    public BuscarOutrasAssinaturasUsuarioUseCase(AssinaturaService assinaturaService) {
        this.assinaturaService = assinaturaService;
    }

    public List<Assinatura> executar(UUID idUsuario) {
        return assinaturaService.buscarOutrasAssinaturasByUsuarioId(idUsuario);
    }
}
