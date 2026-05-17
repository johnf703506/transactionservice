package com.transaction.mapper;

import com.transaction.dto.TreasuryExchangeRate;
import com.transaction.model.CurrencyRate;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CurrencyRateMapperTest {

    private final CurrencyRateMapper mapper =
            Mappers.getMapper(CurrencyRateMapper.class);

    @Test
    void shouldMapTreasuryExchangeRate() {

        TreasuryExchangeRate source = new TreasuryExchangeRate();
        source.setCurrency("EURO");
        source.setEffectiveDate("2026-01-01");
        source.setExchangeRate("1.234500");

        CurrencyRate result = mapper.toEntity(source);

        assertEquals("EURO", result.getCurrencyCode());
        assertEquals(LocalDate.of(2026, 1, 1), result.getEffectiveDate());
        assertEquals(new BigDecimal("1.234500"), result.getExchangeRate());
    }
}