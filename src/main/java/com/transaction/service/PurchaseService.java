package com.transaction.service;

import com.transaction.dto.PurchaseRequest;
import com.transaction.dto.PurchaseResponse;
import com.transaction.model.CurrencyRate;
import com.transaction.model.PurchaseTransaction;
import com.transaction.repository.CurrencyRateRepository;
import com.transaction.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.UUID;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CurrencyRateRepository currencyRateRepository;

    public PurchaseService(PurchaseRepository purchaseRepository,
                           CurrencyRateRepository currencyRateRepository) {
        this.purchaseRepository = purchaseRepository;
        this.currencyRateRepository = currencyRateRepository;
    }

    public PurchaseTransaction save(PurchaseRequest request) {

        if (request.getTransactionDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Transaction date cannot be in the future");
        }

        PurchaseTransaction purchaseTransaction = new PurchaseTransaction();
        purchaseTransaction.setId(UUID.randomUUID().toString());
        purchaseTransaction.setDescription(request.getDescription());
        purchaseTransaction.setTransactionDate(request.getTransactionDate());
        purchaseTransaction.setPurchaseAmount(request.getPurchaseAmount().setScale(2, RoundingMode.HALF_UP));

        return purchaseRepository.save(purchaseTransaction);
    }

    public PurchaseResponse getConverted(String id, String currencyCode) {

        PurchaseTransaction purchaseTransaction = purchaseRepository.findById(id.strip())
                .orElseThrow(() -> new IllegalArgumentException("Purchase not found"));

        CurrencyRate rate = currencyRateRepository
                .findTopByCurrencyCodeAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                        currencyCode.strip().toUpperCase(),
                        purchaseTransaction.getTransactionDate()
                )
                .orElseThrow(() -> new IllegalArgumentException("No valid exchange rate found"));

        if (rate.getEffectiveDate().isBefore(purchaseTransaction.getTransactionDate().minusMonths(6))) {
            throw new IllegalArgumentException("No exchange rate found within 6 months");
        }


        BigDecimal converted = purchaseTransaction.getPurchaseAmount()
                .multiply(rate.getExchangeRate())
                .setScale(2, RoundingMode.HALF_UP);

        return new PurchaseResponse(
                purchaseTransaction.getId(),
                purchaseTransaction.getDescription(),
                purchaseTransaction.getTransactionDate(),
                purchaseTransaction.getPurchaseAmount(),
                rate.getCurrencyCode(),
                rate.getExchangeRate(),
                converted
        );
    }
}