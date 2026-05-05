package com.gmfs.demo.controller;

import com.gmfs.demo.dto.AmountRequest;
import com.gmfs.demo.dto.TransactionMutationResponse;
import com.gmfs.demo.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/{accountId}/deposit")
    public TransactionMutationResponse deposit(
            @PathVariable Long accountId,
            @RequestHeader("X-Auth-Token") String authToken,
            @Valid @RequestBody AmountRequest body
    ) {
        AccountService.MutationResult result = accountService.deposit(accountId, authToken, body.getAmount());
        return new TransactionMutationResponse(result.transactionId(), result.newBalance());
    }

    @PostMapping("/{accountId}/withdraw")
    public TransactionMutationResponse withdraw(
            @PathVariable Long accountId,
            @RequestHeader("X-Auth-Token") String authToken,
            @Valid @RequestBody AmountRequest body
    ) {
        AccountService.MutationResult result = accountService.withdraw(accountId, authToken, body.getAmount());
        return new TransactionMutationResponse(result.transactionId(), result.newBalance());
    }
}
