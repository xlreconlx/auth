package com.prueba.spring_dummyjson.auth.controller;

import com.prueba.spring_dummyjson.auth.dto.LoginRequestDto;
import com.prueba.spring_dummyjson.auth.dto.LoginResponseDto;
import com.prueba.spring_dummyjson.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticacion", description = "Endpoints para login y gestion de tokens")
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "Login de usuario",
            description = "Valida credenciales y retorna tokens JWT",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Login exitoso",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Credenciales inválidas o request malformado",
                            content = @Content(
                                    mediaType = "application/json"
                            )
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto response = authService.login(
                loginRequestDto.getUsername(),
                loginRequestDto.getPassword()
        );
        return ResponseEntity.ok(response);
    }
}
