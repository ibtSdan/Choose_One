package com.example.choose_one.repository;

import com.example.choose_one.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<TokenEntity, Long> {
    boolean existsByUserId(Long userId);
    Optional<TokenEntity> findByRefreshToken(String refreshToken);
}
