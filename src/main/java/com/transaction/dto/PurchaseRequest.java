package com.transaction.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseRequest {

    @NotBlank(message = "Description is required")
    @Size(
            max = 50,
            message = "Description must not exceed 50 characters"
    )
    private String description;

    @NotNull(message = "Transaction date is required")
    private LocalDate transactionDate;

    @NotNull(message = "Purchase amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Purchase amount must be greater than 0"
    )
    @Digits(
            integer = 10,
            fraction = 2,
            message = "Purchase amount must be rounded to 2 decimal places"
    )
    private BigDecimal purchaseAmount;
}