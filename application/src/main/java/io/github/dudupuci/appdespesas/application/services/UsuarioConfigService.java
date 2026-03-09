package io.github.dudupuci.appdespesas.application.services;

import io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.usuario.configuracoes.EditarConfiguracoesUsuarioRequestDto;
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

    public void editarConfiguracoes(UUID usuarioId, EditarConfiguracoesUsuarioRequestDto editarPreferenciasDto) {
        UsuarioSistema usuario = usuarioService.buscarPorId(usuarioId);

        if (editarPreferenciasDto != null) {

            if (editarPreferenciasDto.canaisNotificacao() != null && !editarPreferenciasDto.canaisNotificacao().isEmpty()) {
                usuario.getUsuarioSistemaConfig()
                        .setCanaisNotificacao(editarPreferenciasDto.canaisNotificacao());
            }

            if (editarPreferenciasDto.tomRespostaIA() != null) {
                usuario.getUsuarioSistemaConfig()
                        .setTomRespostaIA(editarPreferenciasDto.tomRespostaIA());
            }

            if (editarPreferenciasDto.idiomaPreferidoIA() != null) {
                usuario.getUsuarioSistemaConfig()
                        .setIdiomaPreferidoIA(editarPreferenciasDto.idiomaPreferidoIA());
            }

            if (!StringUtils.isEmpty(editarPreferenciasDto.instrucoesIA())) {
                usuario.getUsuarioSistemaConfig()
                        .setInstrucoesIA(editarPreferenciasDto.instrucoesIA());
            }
        }

        usuarioService.atualizar(usuario);
    }



}