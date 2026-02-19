package io.github.dudupuci.appdespesas.validation;

import io.github.dudupuci.appdespesas.services.annotations.CpfOuCnpj;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CpfOuCnpjValidator implements ConstraintValidator<CpfOuCnpj, String> {

    private static final Pattern ONLY_DIGITS = Pattern.compile("\\d+");

    @Override
    public void initialize(CpfOuCnpj constraintAnnotation) {
        // no-op
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true; // use @NotBlank / @NotNull if required
        String digits = value.replaceAll("\\D", "");
        if (digits.isEmpty()) return true;

        if (!ONLY_DIGITS.matcher(digits).matches()) return false;

        if (digits.length() == 11) {
            return validarCpf(digits);
        } else if (digits.length() == 14) {
            return validarCnpj(digits);
        }
        return false;
    }

    private boolean validarCpf(String cpf) {
        // reject known invalid sequences
        if (cpf.chars().distinct().count() == 1) return false;

        try {
            int[] nums = cpf.chars().map(c -> c - '0').toArray();

            // first digit
            int sum = 0;
            for (int i = 0; i < 9; i++) sum += nums[i] * (10 - i);
            int r = sum % 11;
            int d1 = (r < 2) ? 0 : 11 - r;
            if (nums[9] != d1) return false;

            // second digit
            sum = 0;
            for (int i = 0; i < 10; i++) sum += nums[i] * (11 - i);
            r = sum % 11;
            int d2 = (r < 2) ? 0 : 11 - r;
            return nums[10] == d2;
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean validarCnpj(String cnpj) {
        // reject known invalid sequences
        if (cnpj.chars().distinct().count() == 1) return false;

        try {
            int[] nums = cnpj.chars().map(c -> c - '0').toArray();

            int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

            int sum = 0;
            for (int i = 0; i < 12; i++) sum += nums[i] * peso1[i];
            int r = sum % 11;
            int d1 = (r < 2) ? 0 : 11 - r;
            if (nums[12] != d1) return false;

            sum = 0;
            for (int i = 0; i < 13; i++) sum += nums[i] * peso2[i];
            r = sum % 11;
            int d2 = (r < 2) ? 0 : 11 - r;
            return nums[13] == d2;
        } catch (Exception ex) {
            return false;
        }
    }
}

