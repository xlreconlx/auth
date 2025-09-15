package com.prueba.spring_dummyjson.auth.service;

import com.prueba.spring_dummyjson.auth.dto.LoginRequestDto;
import com.prueba.spring_dummyjson.auth.dto.LoginResponseDto;
import com.prueba.spring_dummyjson.auth.dto.MeResponseDto;
import com.prueba.spring_dummyjson.auth.entity.LoginLog;
import com.prueba.spring_dummyjson.auth.feign.AuthClient;
import com.prueba.spring_dummyjson.auth.feign.UserClient;
import com.prueba.spring_dummyjson.auth.repository.LoginLogRepository;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthClient authClient;
    private final UserClient userClient;
    private final LoginLogRepository loginRepository;

    public LoginResponseDto login(String username, String password) {
        log.info("Iniciando login para usuario: {}", username);

        LoginRequestDto req = new LoginRequestDto(username, password);
        ResponseEntity<LoginResponseDto> response = authClient.login(req);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            log.warn("Autenticacion fallida para usuario: {}", username);
            throw new RuntimeException("Autenticación fallida");
        }

        LoginResponseDto loginResponse = response.getBody();
        log.debug("Respuesta de login recibida: {}", loginResponse);

        List<String> cookies = response.getHeaders().get("Set-Cookie");
        String accessTokenCookie = null;
        if (cookies != null) {
            accessTokenCookie = cookies.stream()
                    .filter(c -> c.startsWith("accessToken"))
                    .findFirst().orElse(null);
        }
 

        if (accessTokenCookie != null) {
            Optional<MeResponseDto> meOptional = Optional.empty();
            String bearerToken = "Bearer " + loginResponse.getAccessToken();
            log.info("Consultando /auth/me con token para usuario: {}", accessTokenCookie);
            meOptional = Optional.ofNullable(userClient.me(bearerToken));

            meOptional.ifPresentOrElse(
                    me -> log.info("Respuesta de /auth/me: {}", me),
                    () -> log.warn("El endpoint /auth/me no devolvio informacion para el usuario: {}", username)
            );
        }

        LoginLog logEntity = LoginLog.builder()
                .username(loginResponse.getUsername())
                .loginTime(OffsetDateTime.now())
                .accessToken(loginResponse.getAccessToken())
                .refreshToken(loginResponse.getRefreshToken())
                .build();

        loginRepository.save(logEntity);
        log.info("Login registrado en base de datos para usuario: {}", username);

        return loginResponse;
    }

}
