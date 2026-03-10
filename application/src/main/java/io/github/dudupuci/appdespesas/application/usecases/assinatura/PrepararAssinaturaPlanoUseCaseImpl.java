package io.github.dudupuci.appdespesas.application.usecases.assinatura;

import io.github.dudupuci.appdespesas.application.responses.assinatura.CheckoutAssinaturaResult;
import io.github.dudupuci.appdespesas.application.services.AssinaturaService;
import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;

public class PrepararAssinaturaPlanoUseCaseImpl extends PrepararAssinaturaPlanoUseCase {

    private final UsuarioService usuarioService;
    private final AssinaturaService assinaturaService;

    public PrepararAssinaturaPlanoUseCaseImpl(UsuarioService usuarioService, AssinaturaService assinaturaService) {
        this.usuarioService = usuarioService;
        this.assinaturaService = assinaturaService;
    }

    // Command: PrepararAssinaturaCommand(UUID usuarioId, Long assinaturaId)
    @Override
    public CheckoutAssinaturaResult executar(PrepararAssinaturaCommand cmd) {
        UsuarioSistema usuario = usuarioService.buscarPorId(cmd.usuarioId());
        Assinatura assinatura = assinaturaService.buscarAssinaturaPorId(cmd.assinaturaId());
        assinaturaService.validarAssinatura(assinatura, usuario);
        return CheckoutAssinaturaResult.fromAssinatura(assinatura);
    }
}
