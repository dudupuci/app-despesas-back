package io.github.dudupuci.appdespesas.application.usecases.cor;

import java.util.UUID;

/**
 * Command para criar ou atualizar uma cor.
 */
public record CorCommand(
        UUID usuarioId,
        UUID corId, // usado apenas no update/delete
        String nome,
        String codigoHexadecimal
) {}
