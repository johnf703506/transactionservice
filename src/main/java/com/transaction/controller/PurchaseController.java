package com.transaction.controller;

import com.transaction.dto.PurchaseRequest;
import com.transaction.dto.PurchaseResponse;
import com.transaction.model.PurchaseTransaction;
import com.transaction.service.PurchaseService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    private final PurchaseService service;

    public PurchaseController(PurchaseService service) {
        this.service = service;
    }

    @Operation(
            summary = "Create a purchase transaction",
            description = "Saves a new purchase transaction and returns the persisted transaction details."
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseTransaction save(@Valid @RequestBody PurchaseRequest request) {
        return service.save(request);
    }

    @Operation(
            summary = "Retrieve a purchase transaction",
            description = "Retrieves a purchase transaction by ID and converts the amount to the requested currency."
    )
    @GetMapping("/{id}")
    public PurchaseResponse get(@PathVariable String id,
                                @RequestParam String currency) {
        return service.getConverted(id, currency);
    }
}