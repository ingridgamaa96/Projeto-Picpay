package com.projetopicpay.repository;

import com.projetopicpay.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository <Transaction , Long> {
}
