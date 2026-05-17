package com.transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TreasuryExchangeRateResponse {

    private List<TreasuryExchangeRate> data;

    private Meta meta;

    @Data
    public static class Meta {

        @JsonProperty("total-pages")
        private int totalPages;

        @JsonProperty("total-count")
        private int totalCount;

        private int count;
    }
}