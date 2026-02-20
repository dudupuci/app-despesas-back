package io.github.dudupuci.appdespesas.services.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CepValidator implements ConstraintValidator<Cep, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true; // Deixe @NotBlank/@NotNull cuidar disso
        String digits = value.replaceAll("\\D", "");
        return digits.length() == 8;
    }

    /**
     * Método utilitário para remover pontuação e retornar apenas os números do CEP.
     */
    public static String sanitizeCep(String value) {
        if (value == null) return null;
        return value.replaceAll("\\D", "");
    }
}

