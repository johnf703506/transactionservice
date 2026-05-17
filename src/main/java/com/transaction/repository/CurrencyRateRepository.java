package com.transaction.repository;

import com.transaction.model.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {

    Optional<CurrencyRate> findTopByCurrencyCodeAndEffectiveDateLessThanEqualOrderByEffectiveDateDesc(
            String currencyCode,
            LocalDate effectiveDate
    );
}