package com.prueba.spring_dummyjson.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "login_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginLog {
    @Id
    @GeneratedValue
    private UUID id;
    private String username;
    private OffsetDateTime loginTime;
    @Column(length = 2048)
    private String accessToken;
    @Column(length = 2048)
    private String refreshToken;
}
