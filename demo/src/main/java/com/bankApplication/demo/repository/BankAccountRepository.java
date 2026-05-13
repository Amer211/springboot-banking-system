package com.bankApplication.demo.repository;

import com.bankApplication.demo.model.BankAccount;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Integer > {
    boolean existsByAccountNumber(String accountNumber);

    @Nullable List<BankAccount> findByUser_UserId(int userId);
}
