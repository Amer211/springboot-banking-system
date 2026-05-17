package com.bankApplication.demo.repository;

import com.bankApplication.demo.model.BankAccount;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount,Integer > {

    boolean existsByAccountNumber(String accountNumber);

    List<BankAccount> findByUser_UserId(int userId);

    Optional<BankAccount> findByAccountNumber(String AccountNumber);
}
