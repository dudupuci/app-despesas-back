package io.github.dudupuci.appdespesas.services.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = CepValidator.class)
@Target({ FIELD, PARAMETER })
@Retention(RUNTIME)
public @interface Cep {
    String message() default "CEP inválido. Use 8 dígitos ou no formato 99999-999.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
