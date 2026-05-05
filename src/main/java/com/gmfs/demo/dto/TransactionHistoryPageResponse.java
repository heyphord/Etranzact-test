package com.gmfs.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionHistoryPageResponse {

    private List<TransactionHistoryItem> content;
    private int totalPages;
    private long totalElements;
    private int number;
    private int size;
}
