package com.prueba.spring_dummyjson.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "login_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginLog {
    @Id
    @GeneratedValue
    private UUID id;
    private String username;
    private OffsetDateTime loginTime;
    private String accessToken;
    private String refreshToken;
}
