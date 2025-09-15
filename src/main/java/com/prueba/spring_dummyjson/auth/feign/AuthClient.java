/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.prueba.spring_dummyjson.auth.feign;

import com.prueba.spring_dummyjson.auth.dto.LoginRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author ander
 */
@FeignClient(name = "dummy-auth", url = "${dummyjson.base-url}")
public interface AuthClient {
    @PostMapping(value = "/auth/login")
    ResponseEntity<UserClient> login(@RequestBody LoginRequestDto request);
}
