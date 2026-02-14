package io.github.dudupuci.appdespesas.services.validators;

import io.github.dudupuci.appdespesas.services.annotations.WhatsApp;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WhatsAppValidator implements ConstraintValidator<WhatsApp, String> {

    @Override
    public void initialize(WhatsApp constraintAnnotation) {
        // no initialization needed
    }

    @Override
    public boolean isValid(String telefone, ConstraintValidatorContext context) {
        if (telefone == null || telefone.isBlank()) return true; // @NotBlank deve tratar obrigatoriedade quando necessário

        // Remover todos os caracteres não numéricos
        String digits = telefone.replaceAll("\\D", "");

        // Deve ter exatamente 11 dígitos (DDD + 9 dígitos)
        if (digits.length() != 11) return false;

        // O nono dígito (índice 2 do subscriber? actually Brazilian format: DDD (2) + 9-digit where first digit is 9) -> verificamos o primeiro dígito do bloco do assinante (posição 2? )
        // Ex: 44999543420 -> DDD=44, subscriber=999543420 (startsWith 9)
        String subscriber = digits.substring(2);
        if (!subscriber.startsWith("9")) return false;

        // Não aceitar sequências repetidas como 9999999999 (aplicar ao subscriber)
        if (allCharsEqual(subscriber)) return false;

        // Não aceitar sequências repetidas no número inteiro (ex: 44999999999)
        if (allCharsEqual(digits)) return false;

        // Pode adicionar outras regras se necessário
        return true;
    }

    private boolean allCharsEqual(String s) {
        if (s == null || s.isEmpty()) return true;
        char first = s.charAt(0);
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) != first) return false;
        }
        return true;
    }
}

