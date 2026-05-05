package com.gmfs.demo.service;

import com.gmfs.demo.exception.ConflictException;
import com.gmfs.demo.exception.UnauthorizedException;
import com.gmfs.demo.model.Account;
import com.gmfs.demo.model.AccountType;
import com.gmfs.demo.model.AuthToken;
import com.gmfs.demo.model.Customer;
import com.gmfs.demo.repository.AuthTokenRepository;
import com.gmfs.demo.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final int TOKEN_VALID_HOURS = 24;

    private final CustomerRepository customerRepository;
    private final AuthTokenRepository authTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public SignupResult signup(String firstName, String lastName, java.time.LocalDate dob, String idNumber, String pin) {
        if (customerRepository.existsByIdNumber(idNumber)) {
            throw new ConflictException("Customer with this id number already exists");
        }

        Customer customer = Customer.builder()
                .firstName(firstName)
                .lastName(lastName)
                .dob(dob)
                .idNumber(idNumber)
                .pinHash(passwordEncoder.encode(pin))
                .createdAt(Instant.now())
                .build();

        Account primary = Account.builder()
                .type(AccountType.SAVINGS)
                .balance(BigDecimal.ZERO.setScale(4))
                .primary(true)
                .createdAt(Instant.now())
                .customer(customer)
                .build();

        customer.getAccounts().add(primary);
        Customer saved = customerRepository.save(customer);
        Account savedPrimary = saved.getAccounts().stream()
                .filter(Account::isPrimary)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Primary account missing after signup"));

        return new SignupResult(saved.getId(), savedPrimary.getId());
    }

    @Transactional
    public LoginResult login(String idNumber, String pin) {
        Customer customer = customerRepository.findByIdNumber(idNumber)
                .orElseThrow(() -> new UnauthorizedException("Invalid id number or PIN"));

        if (!passwordEncoder.matches(pin, customer.getPinHash())) {
            throw new UnauthorizedException("Invalid id number or PIN");
        }

        authTokenRepository.deleteByCustomerId(customer.getId());

        String token = UUID.randomUUID().toString();
        Instant expiresAt = Instant.now().plus(TOKEN_VALID_HOURS, ChronoUnit.HOURS);
        authTokenRepository.save(AuthToken.builder()
                .token(token)
                .customerId(customer.getId())
                .expiresAt(expiresAt)
                .build());

        return new LoginResult(token, customer.getId(), expiresAt);
    }

    @Transactional(readOnly = true)
    public Long requireCustomerId(String token) {
        if (token == null || token.isBlank()) {
            throw new UnauthorizedException("Missing X-Auth-Token header");
        }
        return authTokenRepository.findByTokenAndExpiresAtAfter(token.trim(), Instant.now())
                .map(AuthToken::getCustomerId)
                .orElseThrow(() -> new UnauthorizedException("Invalid or expired token"));
    }

    public record SignupResult(Long customerId, Long primaryAccountId) {}

    public record LoginResult(String token, Long customerId, Instant expiresAt) {}
}
