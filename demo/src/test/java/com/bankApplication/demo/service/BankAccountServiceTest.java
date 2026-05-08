package com.bankApplication.demo.service;

import com.bankApplication.demo.model.BankAccount;
import com.bankApplication.demo.repository.BankAccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private BankAccountService bankAccountService;






    @Test
    void shouldCreateBankAccount() {
        BankAccount account= new BankAccount(1000);
        account.setId(1);
        account.setAccountNumber("1234");


    }






    @Test
    void isAccountExists() {
    }

    @Test
    void getAllAccount() {
    }

    @Test
    void getBalanceById() {
    }

    @Test
    void deposit() {
    }

    @Test
    void withdraw() {
    }
}