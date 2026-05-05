package com.gmfs.demo.service;

import com.gmfs.demo.exception.NotFoundException;
import com.gmfs.demo.exception.UnauthorizedException;
import com.gmfs.demo.model.Account;
import com.gmfs.demo.model.WalletTransaction;
import com.gmfs.demo.repository.AccountRepository;
import com.gmfs.demo.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final WalletTransactionRepository walletTransactionRepository;
    private final AccountRepository accountRepository;
    private final AuthService authService;

    @Transactional(readOnly = true)
    public Page<WalletTransaction> listForAccount(Long accountId, String authToken, Pageable pageable) {
        Long customerId = authService.requireCustomerId(authToken);
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        if (!account.getCustomer().getId().equals(customerId)) {
            throw new UnauthorizedException("Account does not belong to this customer");
        }
        return walletTransactionRepository.findByAccountIdOrderByOccurredAtDesc(accountId, pageable);
    }
}
