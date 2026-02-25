package io.github.dudupuci.appdespesas.controllers.advice;

import io.github.dudupuci.appdespesas.controllers.advice.dtos.FieldErrorDto;
import io.github.dudupuci.appdespesas.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(AppDespesasException.class)
    public ResponseEntity<ErrorResponse> handleAppException(
            AppDespesasException ex
    ) {

        ErrorResponse body = new ErrorResponse(
                Instant.now().toString(),
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(),
                ex.getMessage()
        );

        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<FieldErrorDto> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> new FieldErrorDto(f.getField(), f.getDefaultMessage()))
                .toList();

        ErrorResponse response = new ErrorResponse(
             Instant.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Erro de validação",
                fieldErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }



}
