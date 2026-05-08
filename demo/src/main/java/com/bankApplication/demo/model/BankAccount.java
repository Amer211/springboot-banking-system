package com.bankApplication.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter


public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private double balance;

    @Column
    private String accountNumber;


    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL,orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<Transaction> transactions = new ArrayList<>();




    public BankAccount(double balance) {
        this.balance = balance;

    }

    public void deposit(double amount){
        if (amount<=0){
            throw new IllegalArgumentException("Deposit must be positive");
        }

        balance+=amount;

    }




    public void withdraw(double amount){
        if (amount>balance){
            throw new RuntimeException("Insufficient funds");
        }else {
            balance-=amount;

        }
    }







}
