package com.transaction.service;

import com.transaction.mapper.CurrencyRateMapper;
import com.transaction.repository.CurrencyRateRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class TreasuryRateLoaderServiceTest {

    @Test
    void shouldCreateService() {

        CurrencyRateRepository repository =
                Mockito.mock(CurrencyRateRepository.class);

        CurrencyRateMapper mapper =
                Mockito.mock(CurrencyRateMapper.class);

        RestClient.Builder builder =
                RestClient.builder();

        TreasuryRateLoaderService service =
                new TreasuryRateLoaderService(
                        repository,
                        mapper,
                        builder
                );

        assertNotNull(service);
    }
}