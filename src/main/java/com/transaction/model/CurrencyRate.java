package com.transaction.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(indexes = @Index(columnList = "currencyCode, effectiveDate"))
@Data
public class CurrencyRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String currencyCode;

    private LocalDate effectiveDate;

    @Column(precision = 20, scale = 6)
    private BigDecimal exchangeRate;

}