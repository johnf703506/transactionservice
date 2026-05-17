package com.transaction.mapper;


import com.transaction.dto.TreasuryExchangeRate;
import com.transaction.model.CurrencyRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper(componentModel = "spring")
public interface CurrencyRateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(
            target = "currencyCode",
            source = "currency",
            qualifiedByName = "toUpperCase"
    )
    @Mapping(
            target = "effectiveDate",
            source = "effectiveDate",
            qualifiedByName = "stringToLocalDate"
    )
    @Mapping(
            target = "exchangeRate",
            source = "exchangeRate",
            qualifiedByName = "stringToBigDecimal"
    )
    CurrencyRate toEntity(TreasuryExchangeRate source);

    @Named("stringToLocalDate")
    default LocalDate stringToLocalDate(String value) {
        return value == null ? null : LocalDate.parse(value);
    }

    @Named("stringToBigDecimal")
    default BigDecimal stringToBigDecimal(String value) {
        return value == null ? null : new BigDecimal(value);
    }

    @Named("toUpperCase")
    default String toUpperCase(String value) {
        return value == null ? null : value.toUpperCase();
    }
}