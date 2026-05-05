package com.gmfs.demo.dto;

import com.gmfs.demo.model.AccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountBalanceResponse {

    private Long accountId;
    private BigDecimal balance;
    private AccountType type;
    private boolean primary;
}
