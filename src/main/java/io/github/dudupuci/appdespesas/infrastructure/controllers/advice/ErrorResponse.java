package io.github.dudupuci.appdespesas.infrastructure.controllers.advice;

import io.github.dudupuci.appdespesas.infrastructure.controllers.advice.dtos.FieldErrorDto;
import lombok.Getter;

import java.util.List;

@Getter
public class ErrorResponse {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private List<FieldErrorDto> fieldErrors;

    public ErrorResponse(String timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ErrorResponse(String timestamp, int status, String error, String message, List<FieldErrorDto> fieldErrors) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.fieldErrors = fieldErrors;

    }
}
