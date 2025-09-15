package com.prueba.spring_dummyjson.auth.exception;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignStatusException(FeignException ex) {
        HttpStatus status = HttpStatus.valueOf(ex.status());

        String responseBody = null;
        if (ex.responseBody().isPresent()) {
            responseBody = new String(ex.responseBody().get().array(), StandardCharsets.UTF_8);
        }

        log.error("Feign error al consumir API externa. Status: {}, Body: {}", status, responseBody, ex);

        if (status == HttpStatus.BAD_REQUEST) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(responseBody != null ? responseBody : "Credenciales invalidas");
        }

        return ResponseEntity
                .status(status)
                .body("Error al consumir API externa: " + (responseBody != null ? responseBody : "sin detalle"));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        log.error("Excepcion en tiempo de ejecucion: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Ocurrio un error interno: " + ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            System.out.println(error.getField() + " = " + error.getRejectedValue());
            errors.put(error.getField(), error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        log.error("Excepcion no controlada: {}", ex.getMessage(), ex);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error inesperado en el servidor");
    }

}
