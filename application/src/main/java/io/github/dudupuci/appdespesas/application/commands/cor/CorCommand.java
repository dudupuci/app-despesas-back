package io.github.dudupuci.appdespesas.application.commands.cor;

/**
 * Command para criar ou atualizar uma cor.
 */
public record CorCommand(
        String nome,
        String codigoHexadecimal
) {}

