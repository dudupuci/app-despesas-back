package io.github.dudupuci.appdespesas.utils;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public final class AppDespesasMessages {

    private static MessageSource messageSource;

    public AppDespesasMessages(MessageSource messageSource) {
        AppDespesasMessages.messageSource = messageSource;
    }

    public static String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, new Locale("pt", "BR"));
    }
}
