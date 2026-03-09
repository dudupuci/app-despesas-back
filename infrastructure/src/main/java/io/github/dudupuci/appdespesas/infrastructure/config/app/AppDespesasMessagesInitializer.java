package io.github.dudupuci.appdespesas.infrastructure.config.app;

import io.github.dudupuci.appdespesas.domain.utils.AppDespesasMessages;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Inicializa o AppDespesasMessages do domain injetando o MessageSource do Spring.
 * Desta forma o domain não conhece o Spring — apenas recebe um BiFunction puro.
 */
@Component
public class AppDespesasMessagesInitializer {

    public AppDespesasMessagesInitializer(MessageSource messageSource) {
        AppDespesasMessages.init((code, args) ->
                messageSource.getMessage(code, args, new Locale("pt", "BR"))
        );
    }
}

