/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.prueba.spring_dummyjson.auth.service;

import com.prueba.spring_dummyjson.auth.dto.MeResponseDto;
import com.prueba.spring_dummyjson.auth.feign.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author ander
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserClient userClient;

    public MeResponseDto getAuthenticatedUser(String accessToken) {
        log.info("Consultando /auth/me con token: {}", accessToken);
        return userClient.me(accessToken);
    }
}
