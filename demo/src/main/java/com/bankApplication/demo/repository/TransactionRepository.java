package com.bankApplication.demo.repository;

import com.bankApplication.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,Long> {
    Optional<Transaction> findByReferenceId(String referenceId);
}