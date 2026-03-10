package io.github.dudupuci.appdespesas.application.usecases.usuario;

import io.github.dudupuci.appdespesas.application.services.UsuarioService;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class EditarConfiguracoesUseCaseImpl extends EditarConfiguracoesUseCase {

    private final UsuarioService usuarioService;
    private final UUID usuarioId;

    public EditarConfiguracoesUseCaseImpl(UsuarioService usuarioService, UUID usuarioId) {
        this.usuarioService = usuarioService;
        this.usuarioId = usuarioId;
    }

    @Override
    public void executar(EditarConfiguracoesCommand cmd) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);
        if (cmd == null) return;
        if (cmd.canaisNotificacao() != null && !cmd.canaisNotificacao().isEmpty()) {
            usuario.getUsuarioSistemaConfig().setCanaisNotificacao(cmd.canaisNotificacao());
        }
        if (cmd.tomRespostaIA() != null) {
            usuario.getUsuarioSistemaConfig().setTomRespostaIA(cmd.tomRespostaIA());
        }
        if (cmd.idiomaPreferidoIA() != null) {
            usuario.getUsuarioSistemaConfig().setIdiomaPreferidoIA(cmd.idiomaPreferidoIA());
        }
        if (!StringUtils.isEmpty(cmd.instrucoesIA())) {
            usuario.getUsuarioSistemaConfig().setInstrucoesIA(cmd.instrucoesIA());
        }
        usuarioService.atualizar(usuario);
    }
}

