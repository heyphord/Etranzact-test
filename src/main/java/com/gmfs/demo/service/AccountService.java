package com.gmfs.demo.service;

import com.gmfs.demo.exception.NotFoundException;
import com.gmfs.demo.exception.UnauthorizedException;
import com.gmfs.demo.model.Account;
import com.gmfs.demo.model.TransactionType;
import com.gmfs.demo.model.WalletTransaction;
import com.gmfs.demo.repository.AccountRepository;
import com.gmfs.demo.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final AuthService authService;

    @Transactional
    public MutationResult deposit(Long accountId, String authToken, BigDecimal amount) {
        validatePositiveAmount(amount);
        Long customerId = authService.requireCustomerId(authToken);
        Account account = loadAccountOwnedByCustomer(accountId, customerId);

        BigDecimal before = account.getBalance();
        BigDecimal after = before.add(amount);
        account.setBalance(after);

        WalletTransaction tx = WalletTransaction.builder()
                .type(TransactionType.DEPOSIT)
                .amount(amount)
                .balanceBefore(before)
                .balanceAfter(after)
                .occurredAt(Instant.now())
                .account(account)
                .build();

        walletTransactionRepository.save(tx);
        accountRepository.save(account);

        return new MutationResult(tx.getId(), after);
    }

    @Transactional
    public MutationResult withdraw(Long accountId, String authToken, BigDecimal amount) {
        validatePositiveAmount(amount);
        Long customerId = authService.requireCustomerId(authToken);
        Account account = loadAccountOwnedByCustomer(accountId, customerId);

        BigDecimal before = account.getBalance();
        if (before.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        BigDecimal after = before.subtract(amount);
        account.setBalance(after);

        WalletTransaction tx = WalletTransaction.builder()
                .type(TransactionType.WITHDRAW)
                .amount(amount)
                .balanceBefore(before)
                .balanceAfter(after)
                .occurredAt(Instant.now())
                .account(account)
                .build();

        walletTransactionRepository.save(tx);
        accountRepository.save(account);

        return new MutationResult(tx.getId(), after);
    }

    private static void validatePositiveAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
    }

    private Account loadAccountOwnedByCustomer(Long accountId, Long customerId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        if (!account.getCustomer().getId().equals(customerId)) {
            throw new UnauthorizedException("Account does not belong to this customer");
        }
        return account;
    }

    public record MutationResult(Long transactionId, BigDecimal newBalance) {}
}
