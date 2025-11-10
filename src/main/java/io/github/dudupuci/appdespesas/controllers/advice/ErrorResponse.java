package io.github.dudupuci.appdespesas.controllers.advice;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private String timestamp;
    private int status;
    private String error;
    private String message;

    public ErrorResponse(String timestamp, int status, String error, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
    }
}
