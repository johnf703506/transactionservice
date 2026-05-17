package com.transaction.controller;

import com.transaction.service.TreasuryRateLoaderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/treasury")
@RequiredArgsConstructor
public class TreasuryRateController {

    private final TreasuryRateLoaderService treasuryRateLoaderService;

    @Operation(
            summary = "Refresh treasury exchange rates",
            description = "Fetches and reloads the latest treasury exchange rates into the system."
    )
    @PostMapping("/refresh")
    public ResponseEntity<String> refreshRates() {

        treasuryRateLoaderService.refreshRates();
        return ResponseEntity.ok("Treasury exchange rates refreshed successfully.");
    }
}