package com.transaction.repository;

import com.transaction.model.PurchaseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface PurchaseRepository extends JpaRepository<PurchaseTransaction, String> {


    @Query("""
    SELECT SUM(p.purchaseAmount)
    FROM PurchaseTransaction p
    WHERE YEAR(p.transactionDate) = :year
      AND MONTH(p.transactionDate) = :month
""")
    BigDecimal findTotalsByMonth(
            @Param("year") int year,
            @Param("month") int month
    );
}