package com.transaction.service;

import com.transaction.dto.TreasuryExchangeRate;
import com.transaction.dto.TreasuryExchangeRateResponse;
import com.transaction.mapper.CurrencyRateMapper;
import com.transaction.model.CurrencyRate;
import com.transaction.repository.CurrencyRateRepository;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class TreasuryRateLoaderService {

    private static final String BASE_URL =
            "https://api.fiscaldata.treasury.gov";

    private static final String API_PATH =
            "/services/api/fiscal_service/v1/accounting/od/rates_of_exchange";

    private final CurrencyRateRepository currencyRateRepository;
    private final RestClient restClient;
    private final CurrencyRateMapper currencyRateMapper;

    public TreasuryRateLoaderService(CurrencyRateRepository currencyRateRepository,
                                     CurrencyRateMapper currencyRateMapper,
                                     RestClient.Builder builder) {

        this.currencyRateRepository = currencyRateRepository;
        this.currencyRateMapper = currencyRateMapper;
        this.restClient = builder.baseUrl(BASE_URL).build();
    }

    @PostConstruct
    public void initialize() {

        if (currencyRateRepository.count() == 0) {
            loadRates();
        }
    }

    public void refreshRates() {
        currencyRateRepository.deleteAll();
        loadRates();
    }

    private void loadRates() {

        List<TreasuryExchangeRate> treasuryExchangeRateList =
                getAllExchangeRates();

        treasuryExchangeRateList.forEach(exchangeRate -> {

            CurrencyRate currencyRate =
                    currencyRateMapper.toEntity(exchangeRate);

            currencyRateRepository.save(currencyRate);
        });

        log.info("Loaded {} exchange rates",
                treasuryExchangeRateList.size());
    }

    public List<TreasuryExchangeRate> getAllExchangeRates() {

        LocalDate sevenMonthsAgo = LocalDate.now().minusMonths(7);

        TreasuryExchangeRateResponse response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(API_PATH)
                        .queryParam(
                                "fields",
                                "effective_date,exchange_rate,currency"
                        )
                        .queryParam(
                                "filter",
                                "effective_date:gte:" + sevenMonthsAgo
                        )
                        .queryParam(
                                "sort",
                                "-effective_date"
                        )
                        .queryParam(
                                "page[size]",
                                5000
                        )
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        if (response == null || response.getData() == null) {
            return Collections.emptyList();
        }

        return response.getData();
    }

    @Data
    public static class TreasuryApiResponse {

        private List<TreasuryExchangeRate> data;
    }
}