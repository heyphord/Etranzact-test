package com.gmfs.demo.repository;

import com.gmfs.demo.model.WalletTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

    Page<WalletTransaction> findByAccountIdOrderByOccurredAtDesc(Long accountId, Pageable pageable);
}
