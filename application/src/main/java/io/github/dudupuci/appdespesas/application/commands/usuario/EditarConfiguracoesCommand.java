package io.github.dudupuci.appdespesas.application.commands.usuario;

import io.github.dudupuci.appdespesas.domain.enums.CanalNotificacao;
import io.github.dudupuci.appdespesas.domain.enums.Idioma;
import io.github.dudupuci.appdespesas.domain.enums.TomRespostaIA;

import java.util.Set;

/**
 * Command para editar configurações do usuário.
 */
public record EditarConfiguracoesCommand(
        Set<CanalNotificacao> canaisNotificacao,
        TomRespostaIA tomRespostaIA,
        Idioma idiomaPreferidoIA,
        String instrucoesIA
) {}

