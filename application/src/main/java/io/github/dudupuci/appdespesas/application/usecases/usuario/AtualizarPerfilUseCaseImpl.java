package io.github.dudupuci.appdespesas.application.usecases.usuario;

import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import io.github.dudupuci.appdespesas.domain.exceptions.EntityNotFoundException;

import java.util.UUID;

public class AtualizarPerfilUseCaseImpl extends AtualizarPerfilUseCase {

    private final UsuarioService usuarioService;
    private final UUID usuarioAutenticadoId;

    public AtualizarPerfilUseCaseImpl(UsuarioService usuarioService, UUID usuarioAutenticadoId) {
        this.usuarioService = usuarioService;
        this.usuarioAutenticadoId = usuarioAutenticadoId;
    }

    @Override
    public UsuarioSistema executar(AtualizarUsuarioCommand cmd) {
        return usuarioService.atualizar(usuarioAutenticadoId, cmd);
    }
}

