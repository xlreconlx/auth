/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.prueba.spring_dummyjson.auth.feign;

import com.prueba.spring_dummyjson.auth.dto.MeResponseDto;
import com.prueba.spring_dummyjson.auth.dto.UserResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 *
 * @author ander
 */
@FeignClient(name = "dummy-user", url = "${dummyjson.base-url}")
public interface UserClient {
    @GetMapping("/auth/me")
    MeResponseDto me(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("/users")
    UserResponseDto users();
}
