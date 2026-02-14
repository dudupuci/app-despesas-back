package io.github.dudupuci.appdespesas.services.annotations;

import io.github.dudupuci.appdespesas.services.validators.WhatsAppValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = WhatsAppValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface WhatsApp {
    String message() default "Telefone inválido para WhatsApp. Formato esperado: DDD + 9 dígitos (ex: 44999543420). Não é permitido sequência repetida.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
