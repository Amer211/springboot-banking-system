package com.bankApplication.demo.advice;

public class InsufficientFundsException extends RuntimeException{
    public InsufficientFundsException(){
        super("Insufficient funds");


    }
}
