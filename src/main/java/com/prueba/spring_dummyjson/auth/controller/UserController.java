package com.prueba.spring_dummyjson.auth.controller;

import com.prueba.spring_dummyjson.auth.dto.MeResponseDto;
import com.prueba.spring_dummyjson.auth.dto.UserResponseDto;
import com.prueba.spring_dummyjson.auth.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Usuario", description = "Endpoints para usurios")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Obtener usuario autenticado",
            description = "Retorna la informacion del usuario autenticado usando el token de acceso",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuario obtenido correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MeResponseDto.class),
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            value = """
                                                {
                                                    "email": "michael.williams@x.dummyjson.com",
                                                    "firstName": "Michael",
                                                    "id": 2,
                                                    "lastName": "Williams",
                                                    "username": "michaelw"
                                                }
                                        """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Token invalido o expirado"),
                    @ApiResponse(responseCode = "500", description = "Error interno al obtener el usuario")
            }
    )
    @GetMapping("/me")
    public ResponseEntity<MeResponseDto> me(@RequestHeader("Authorization") String accessToken) {
        MeResponseDto user = userService.getAuthenticatedUser(accessToken);
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Obtener todos los usuarios de prueba",
            description = "Retorna la lista completa de usuarios de prueba desde el servicio externo",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResponseDto.class),
                                    examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            value = """
                        {
                          "users": [
                            {
                              "id": 1,
                              "username": "kminchelle",
                              "email": "kminchelle@gmail.com",
                              "firstName": "Kamren",
                              "lastName": "Minchelle"
                            },
                            {
                              "id": 2,
                              "username": "rfernandez",
                              "email": "rfernandez@gmail.com",
                              "firstName": "Rafael",
                              "lastName": "Fernandez"
                            }
                          ]
                        }
                        """
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "Error interno al obtener los usuarios")
            }
    )
    @GetMapping("/all")
    public ResponseEntity<UserResponseDto> getAllUser() {
        UserResponseDto userResponse = userService.getAllUsers();
        return ResponseEntity.ok(userResponse);
    }

}
