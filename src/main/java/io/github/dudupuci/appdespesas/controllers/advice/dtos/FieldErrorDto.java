package io.github.dudupuci.appdespesas.controllers.advice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FieldErrorDto {
    private String field;
    private String message;

    public FieldErrorDto(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
