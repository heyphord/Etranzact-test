package com.gmfs.demo.repository;

import com.gmfs.demo.model.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.time.Instant;
import java.util.Optional;

public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

    Optional<AuthToken> findByTokenAndExpiresAtAfter(String token, Instant now);

    @Modifying
    void deleteByCustomerId(Long customerId);
}
