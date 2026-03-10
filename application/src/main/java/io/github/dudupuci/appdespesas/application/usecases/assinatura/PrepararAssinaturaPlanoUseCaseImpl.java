package io.github.dudupuci.appdespesas.application.usecases.assinatura;

import io.github.dudupuci.appdespesas.application.responses.assinatura.CheckoutAssinaturaResult;
import io.github.dudupuci.appdespesas.application.services.AssinaturaService;
import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.domain.entities.Assinatura;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;

import java.util.UUID;

public class PrepararAssinaturaPlanoUseCaseImpl extends PrepararAssinaturaPlanoUseCase {

    private final UsuarioService usuarioService;
    private final AssinaturaService assinaturaService;
    private final UUID usuarioIdLogado;

    public PrepararAssinaturaPlanoUseCaseImpl(
            UsuarioService usuarioService,
            AssinaturaService assinaturaService,
            UUID usuarioIdLogado
    ) {
        this.usuarioService = usuarioService;
        this.assinaturaService = assinaturaService;
        this.usuarioIdLogado = usuarioIdLogado;
    }

    @Override
    public CheckoutAssinaturaResult executar(Long assinaturaId) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioIdLogado);
        Assinatura assinatura = assinaturaService.buscarAssinaturaPorId(assinaturaId);
        assinaturaService.validarAssinatura(assinatura, usuario);
        return CheckoutAssinaturaResult.fromAssinatura(assinatura);
    }
}

