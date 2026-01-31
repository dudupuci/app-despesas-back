package io.github.dudupuci.appdespesas.controllers.advice;

import io.github.dudupuci.appdespesas.exceptions.AppDespesasException;
import io.github.dudupuci.appdespesas.exceptions.CategoriaJaExisteException;
import io.github.dudupuci.appdespesas.exceptions.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(AppDespesasException.class)
    public ResponseEntity<ErrorResponse> handleAppException(AppDespesasException ex, HttpServletRequest req) {
        ErrorResponse body = new ErrorResponse(
                Instant.now().toString(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(CategoriaJaExisteException.class)
    public ResponseEntity<ErrorResponse> handleCategoriaJaExistente(CategoriaJaExisteException ex) {
        ErrorResponse body = new ErrorResponse(
                Instant.now().toString(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(EntityNotFoundException ex) {
        ErrorResponse body = new ErrorResponse(
                Instant.now().toString(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }


}
