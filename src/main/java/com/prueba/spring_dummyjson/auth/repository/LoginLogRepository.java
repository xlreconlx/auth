package com.prueba.spring_dummyjson.auth.repository;

import com.prueba.spring_dummyjson.auth.entity.LoginLog;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginLogRepository extends JpaRepository<LoginLog, UUID>{
}
