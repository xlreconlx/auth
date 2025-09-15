package com.prueba.spring_dummyjson.auth.controller;

import com.prueba.spring_dummyjson.auth.dto.LoginRequestDto;
import com.prueba.spring_dummyjson.auth.dto.LoginResponseDto;
import com.prueba.spring_dummyjson.auth.service.AuthService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Test
    void login_Success() throws Exception {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("michaelw");
        request.setPassword("michaelwpass");

        LoginResponseDto response = new LoginResponseDto();
        response.setAccessToken("token123");
        response.setRefreshToken("refresh123");
        response.setUsername("michaelw");

        when(authService.login("michaelw", "michaelwpass")).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("token123"))
                .andExpect(jsonPath("$.refreshToken").value("refresh123"))
                .andExpect(jsonPath("$.username").value("michaelw"));
    }

    @Test
    void login_MissingPassword_ShouldReturnBadRequest() throws Exception {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("michaelw");
        request.setPassword(""); // password vacio

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest()); // Spring Validation deberia disparar
    }

    @Test
    void login_MissingUsername_ShouldReturnBadRequest() throws Exception {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername(""); // username vacio
        request.setPassword("secret");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
