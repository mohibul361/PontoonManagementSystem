package com.biwta.pontoon.repository;

import com.biwta.pontoon.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author nasimkabir
 * ২৮/১১/২৩
 */
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
