package com.transaction.repository;

import com.transaction.model.PurchaseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<PurchaseTransaction, String> {
}