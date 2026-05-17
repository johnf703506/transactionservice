package com.transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.dto.PurchaseRequest;
import com.transaction.service.PurchaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PurchaseController.class)
class PurchaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PurchaseService purchaseService;

    @Test
    void shouldCreatePurchase() throws Exception {

        PurchaseRequest request = new PurchaseRequest(
                "Some Purchase",
                LocalDate.now().minusDays(1),
                new BigDecimal("10.00")
        );

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldFailWhenDescriptionIsBlank() throws Exception {

        PurchaseRequest request = new PurchaseRequest(
                "",
                LocalDate.now().minusDays(1),
                new BigDecimal("10.00")
        );

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description")
                        .value("Description is required"));
    }

    @Test
    void shouldFailWhenDescriptionTooLong() throws Exception {

        PurchaseRequest request = new PurchaseRequest(
                "123456789012345678901234567890123456789012345678901",
                LocalDate.now().minusDays(1),
                new BigDecimal("10.00")
        );

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description")
                        .value("Description must not exceed 50 characters"));
    }

    @Test
    void shouldFailWhenTransactionDateIsNull() throws Exception {

        PurchaseRequest request = new PurchaseRequest(
                "Some Purchase",
                null,
                new BigDecimal("10.00")
        );

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.transactionDate")
                        .value("Transaction date is required"));
    }

    @Test
    void shouldFailWhenPurchaseAmountIsNull() throws Exception {

        PurchaseRequest request = new PurchaseRequest(
                "Some Purchase",
                LocalDate.now().minusDays(1),
                null
        );

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.purchaseAmount")
                        .value("Purchase amount is required"));
    }

    @Test
    void shouldFailWhenPurchaseAmountIsZero() throws Exception {

        PurchaseRequest request = new PurchaseRequest(
                "Some Purchase",
                LocalDate.now().minusDays(1),
                new BigDecimal("0.00")
        );

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.purchaseAmount")
                        .value("Purchase amount must be greater than 0"));
    }

    @Test
    void shouldFailWhenPurchaseAmountIsNegative() throws Exception {

        PurchaseRequest request = new PurchaseRequest(
                "Some Purchase",
                LocalDate.now().minusDays(1),
                new BigDecimal("-5.00")
        );

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.purchaseAmount")
                        .value("Purchase amount must be greater than 0"));
    }

    @Test
    void shouldFailWhenPurchaseAmountHasTooManyDecimals() throws Exception {

        PurchaseRequest request = new PurchaseRequest(
                "Some Purchase",
                LocalDate.now().minusDays(1),
                new BigDecimal("12.001")
        );

        mockMvc.perform(post("/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.purchaseAmount")
                        .value("Purchase amount must be rounded to 2 decimal places"));
    }
}