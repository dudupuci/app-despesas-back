package io.github.dudupuci.appdespesas.application.commands.role;

/**
 * Command para criar uma role.
 */
public record CriarRoleCommand(
        String nome,
        String descricao,
        Integer poder
) {}

