package io.github.dudupuci.appdespesas.domain.utils;

import java.util.function.BiFunction;

/**
 * Utilitário de mensagens do domain.
 * Não possui dependência de nenhum framework — recebe um provider injetado
 * pela infrastructure via {@link #init(BiFunction)}.
 */
public final class AppDespesasMessages {

    private static BiFunction<String, Object[], String> provider;

    private AppDespesasMessages() {}

    /**
     * Chamado pela infrastructure (AppDespesasMessagesInitializer) ao subir o contexto.
     */
    public static void init(BiFunction<String, Object[], String> messageProvider) {
        AppDespesasMessages.provider = messageProvider;
    }

    public static String getMessage(String code, Object[] args) {
        if (provider == null) throw new IllegalStateException("AppDespesasMessages não foi inicializado.");
        return provider.apply(code, args);
    }

    public static String getMessage(String code) {
        return getMessage(code, null);
    }
}
