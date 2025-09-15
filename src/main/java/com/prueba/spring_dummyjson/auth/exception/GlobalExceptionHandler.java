package com.prueba.spring_dummyjson.auth.exception;

import com.prueba.spring_dummyjson.auth.dto.ErrorDto;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorDto> handleFeignStatusException(FeignException ex) {
        HttpStatus status = HttpStatus.resolve(ex.status());

        String responseBody = null;
        if (ex.responseBody().isPresent()) {
            responseBody = new String(ex.responseBody().get().array(), StandardCharsets.UTF_8);
        }

        log.warn("Feign error al consumir API externa. Status: {}, Body: {}", status, responseBody);

        // Extraer solo el mensaje si viene en formato JSON {"message":"..."}
        String message = responseBody;
        if (responseBody != null && responseBody.startsWith("{") && responseBody.contains("message")) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(responseBody);
                if (jsonNode.has("message")) {
                    message = jsonNode.get("message").asText();
                }
            } catch (Exception ignored) {
                System.out.println("Error: " + ignored.getMessage());
            }
        }

        // Caso de token invalido
        if (message != null && message.contains("invalid signature")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    ErrorDto.builder()
                            .timestamp(LocalDateTime.now())
                            .status(HttpStatus.UNAUTHORIZED.value())
                            .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                            .message("Token invalido o expirado. Por favor, autenticarse nuevamente.")
                            .build()
            );
        }

        // Manejo por status HTTP
        if (status != null) {
            switch (status) {
                case BAD_REQUEST, UNAUTHORIZED -> message = message != null ? message : "Credenciales invalidas o token invalido";
                case INTERNAL_SERVER_ERROR -> message = message != null ? message : "Error interno del servicio externo";
                case NOT_FOUND -> message = "Recurso no encontrado en el servicio externo";
                default -> message = message != null ? message : "Error desconocido en el servicio externo";
            }

            return ResponseEntity.status(status).body(
                    ErrorDto.builder()
                            .timestamp(LocalDateTime.now())
                            .status(status.value())
                            .error(status.getReasonPhrase())
                            .message(message)
                            .build()
            );
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorDto.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .message(message != null ? message : "Error al consumir API externa")
                        .build()
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDto> handleRuntimeException(RuntimeException ex) {
        log.warn("Excepcion en tiempo de ejecucion: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorDto.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .message("Ocurrio un error interno: " + ex.getMessage())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder sb = new StringBuilder();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                sb.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ")
        );

        log.warn("Errores de validacion: {}", sb.toString());

        return ResponseEntity.badRequest().body(
                ErrorDto.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message("Errores de validacion")
                        .error(sb.toString())
                        .build()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception ex) {
        log.warn("Excepcion no controlada: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ErrorDto.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .message("Error inesperado en el servidor")
                        .build()
        );
    }

}
