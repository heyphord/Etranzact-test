package com.gmfs.demo.controller;

import com.gmfs.demo.dto.TransactionHistoryItem;
import com.gmfs.demo.dto.TransactionHistoryPageResponse;
import com.gmfs.demo.model.WalletTransaction;
import com.gmfs.demo.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{accountId}/transactions")
    public TransactionHistoryPageResponse history(
            @PathVariable Long accountId,
            @RequestHeader("X-Auth-Token") String authToken,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<WalletTransaction> p = transactionService.listForAccount(accountId, authToken, PageRequest.of(page, size));
        List<TransactionHistoryItem> items = p.getContent().stream()
                .map(tx -> new TransactionHistoryItem(
                        tx.getId(),
                        tx.getType(),
                        tx.getAmount(),
                        tx.getBalanceBefore(),
                        tx.getBalanceAfter(),
                        tx.getOccurredAt()
                ))
                .toList();
        return new TransactionHistoryPageResponse(items, p.getTotalPages(), p.getTotalElements(), p.getNumber(), p.getSize());
    }
}
