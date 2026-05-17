package com.transaction.service;

import com.transaction.dto.PurchaseRequest;
import com.transaction.dto.PurchaseResponse;
import com.transaction.model.CurrencyRate;
import com.transaction.model.PurchaseTransaction;
import com.transaction.repository.CurrencyRateRepository;
import com.transaction.repository.PurchaseRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PurchaseServiceTest {

    @Test
    void shouldSavePurchase() {

        PurchaseRepository purchaseRepository =
                Mockito.mock(PurchaseRepository.class);
        CurrencyRateRepository currencyRateRepository =
                Mockito.mock(CurrencyRateRepository.class);
        PurchaseService service =
                new PurchaseService(purchaseRepository, currencyRateRepository);
        PurchaseRequest request = new PurchaseRequest(
                "Coffee",
                LocalDate.now().minusDays(1),
                new BigDecimal("15.00")
        );

        PurchaseTransaction transaction = new PurchaseTransaction();
        transaction.setDescription("Coffee");

        Mockito.when(purchaseRepository.save(Mockito.any()))
                .thenReturn(transaction);

        PurchaseTransaction result = service.save(request);

        assertEquals("Coffee", result.getDescription());
    }

    @Test
    void shouldConvertPurchase() {

        PurchaseRepository purchaseRepository =
                Mockito.mock(PurchaseRepository.class);

        CurrencyRateRepository currencyRateRepository =
                Mockito.mock(CurrencyRateRepository.class);

        PurchaseService service =
                new PurchaseService(purchaseRepository, currencyRateRepository);

        PurchaseTransaction transaction = new PurchaseTransaction();
        transaction.setPurchaseAmount(new BigDecimal("10.00"));
        transaction.setTransactionDate(LocalDate.now().minusDays(1));
        transaction.setDescription("Coffee");

        CurrencyRate rate = new CurrencyRate();
        rate.setCurrencyCode("EUR");
        rate.setEffectiveDate(LocalDate.now().minusDays(1));
        rate.setExchangeRate(new BigDecimal("0.90"));

        Mockito.when(purchaseRepository.findById("1"))
                .thenReturn(Optional.of(transaction));
        Mockito.when(currencyRateRepository
                .findTopByCurrencyCodeAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
                        Mockito.anyString(),
                        Mockito.any()))
                .thenReturn(Optional.of(rate));

        PurchaseResponse response = service.getConverted("1", "EUR");
        assertEquals(new BigDecimal("9.00"), response.getConvertedAmount());
    }
}