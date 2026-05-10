package com.bankApplication.demo.advice;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(int id){
        super("Account not found with id: "+id);


    }
}
