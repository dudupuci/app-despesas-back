package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.application.commands.usuario.EditarConfiguracoesCommand;
import io.github.dudupuci.appdespesas.domain.entities.UsuarioSistema;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsuarioConfigService {

    private final UsuarioService usuarioService;

    public UsuarioConfigService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public void editarConfiguracoes(UUID usuarioId, EditarConfiguracoesCommand cmd) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);

        if (cmd != null) {
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
        }

        usuarioService.atualizar(usuario);
    }

}