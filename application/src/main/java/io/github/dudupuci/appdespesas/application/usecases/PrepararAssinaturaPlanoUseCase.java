package io.github.dudupuci.appdespesas.application.usecases;

import io.github.dudupuci.appdespesas.application.responses.assinatura.CheckoutAssinaturaResult;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.application.services.AssinaturaService;
import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PrepararAssinaturaPlanoUseCase {

    private final UsuarioService usuarioService;
    private final AssinaturaService assinaturaService;

    public PrepararAssinaturaPlanoUseCase(UsuarioService usuarioService, AssinaturaService assinaturaService) {
        this.usuarioService = usuarioService;
        this.assinaturaService = assinaturaService;
    }

    public CheckoutAssinaturaResult executar(UUID usuarioIdLogado, Long assinaturaId) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioIdLogado);
        Assinatura assinatura = assinaturaService.buscarAssinaturaPorId(assinaturaId);
        assinaturaService.validarAssinatura(assinatura, usuario);
        return CheckoutAssinaturaResult.fromAssinatura(assinatura);
    }

}
