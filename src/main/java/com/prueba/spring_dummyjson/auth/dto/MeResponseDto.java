package com.prueba.spring_dummyjson.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeResponseDto {
    private int id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
}
