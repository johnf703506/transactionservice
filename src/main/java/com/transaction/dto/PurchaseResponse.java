package com.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PurchaseResponse {

    private String id;
    private String description;
    private LocalDate transactionDate;
    private BigDecimal originalAmountUsd;
    private String currencyCode;
    private BigDecimal exchangeRate;
    private BigDecimal convertedAmount;
}