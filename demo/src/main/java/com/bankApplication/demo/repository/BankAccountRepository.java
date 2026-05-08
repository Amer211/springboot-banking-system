package com.bankApplication.demo.repository;

import com.bankApplication.demo.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Integer > {
    boolean existsByAccountNumber(String accountNumber);
}
