package io.github.dudupuci.appdespesas.application.services.annotations;

import io.github.dudupuci.appdespesas.domain.validation.CpfOuCnpjValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = CpfOuCnpjValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface CpfOuCnpj {
    String message() default "Documento inválido: deve ser CPF (11 dígitos) ou CNPJ (14 dígitos).";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

