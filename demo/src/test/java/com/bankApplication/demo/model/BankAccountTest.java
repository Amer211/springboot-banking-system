package com.bankApplication.demo.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountTest {

    @Test
    void deposit_shouldIncreaseBalance() {
        BankAccount account = new BankAccount(100.0);
        account.deposit(50.0);
        assertEquals(150.0, account.getBalance());
        
    }

    @Test
    void withdraw_shouldDecreaseBalance() {
        BankAccount account = new BankAccount(100.0);
        account.withdraw(30.0);
        assertEquals(70.0, account.getBalance());
    }

    @Test
    void withdraw_shouldThrowExceptionWhenInsufficientFunds() {
        BankAccount account = new BankAccount(100.0);
        assertThrows(RuntimeException.class, () -> account.withdraw(150.0));
    }
    @Test
    void deposit_shouldThrowExceptionWhenNegativeDeposit(){
        BankAccount account = new BankAccount(100);

        assertThrows(RuntimeException.class,()->account.deposit(0));


    }







}