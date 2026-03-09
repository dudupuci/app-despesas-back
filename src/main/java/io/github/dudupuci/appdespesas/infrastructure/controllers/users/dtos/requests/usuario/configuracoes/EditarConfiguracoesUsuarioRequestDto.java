package io.github.dudupuci.appdespesas.infrastructure.controllers.users.dtos.requests.usuario.configuracoes;

import io.github.dudupuci.appdespesas.domain.enums.CanalNotificacao;
import io.github.dudupuci.appdespesas.domain.enums.Idioma;
import io.github.dudupuci.appdespesas.domain.enums.TomRespostaIA;

import java.util.Set;

public record EditarConfiguracoesUsuarioRequestDto(
        Set<CanalNotificacao> canaisNotificacao,
        TomRespostaIA tomRespostaIA,
        Idioma idiomaPreferidoIA,
        String instrucoesIA
) {
}
