package com.prueba.spring_dummyjson.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private List<MeResponseDto> users;
    private int total;
    private int skip;
    private int limit;
}
