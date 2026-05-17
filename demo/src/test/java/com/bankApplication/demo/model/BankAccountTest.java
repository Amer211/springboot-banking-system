package com.bankApplication.demo.model;

import org.junit.jupiter.api.Test;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void deposit_shouldIncreaseBalance() {
        BankAccount account = new BankAccount
                (new BigDecimal("100"), AccountType.COMPLETE_CHECKING);
        account.deposit(new BigDecimal("50"));
        assertEquals(new BigDecimal("150"), account.getBalance());
        
    }

    @Test
    void withdraw_shouldDecreaseBalance() {
        BankAccount account = new BankAccount(new BigDecimal("100"), AccountType.COMPLETE_CHECKING);
        account.withdraw(new BigDecimal("30"));
        assertEquals(new BigDecimal("70"), account.getBalance());
    }

    @Test
    void withdraw_shouldThrowExceptionWhenInsufficientFunds() {
        BankAccount account = new BankAccount(new BigDecimal("100"),AccountType.COMPLETE_CHECKING);
        assertThrows(RuntimeException.class, () -> account.withdraw(new BigDecimal("150")));
    }
    @Test
    void deposit_shouldThrowExceptionWhenNegativeDeposit(){
        BankAccount account = new BankAccount(new BigDecimal("100"), AccountType.COMPLETE_CHECKING);

        assertThrows(RuntimeException.class,()->account.deposit(new BigDecimal("-50")));


    }







}