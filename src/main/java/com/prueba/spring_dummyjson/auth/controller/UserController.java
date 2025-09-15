package com.prueba.spring_dummyjson.auth.controller;

import com.prueba.spring_dummyjson.auth.dto.MeResponseDto;
import com.prueba.spring_dummyjson.auth.service.UserService;
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

    @GetMapping("/me")
    public ResponseEntity<MeResponseDto> me(@RequestHeader("Authorization") String accessToken) {
        System.out.println(accessToken);
        MeResponseDto user = userService.getAuthenticatedUser(accessToken);
        return ResponseEntity.ok(user);
    }

}
