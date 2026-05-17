package com.transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TreasuryExchangeRate {

    @JsonProperty("effective_date")
    private String effectiveDate;

    @JsonProperty("exchange_rate")
    private String exchangeRate;

    @JsonProperty("currency")
    private String currency;

}
