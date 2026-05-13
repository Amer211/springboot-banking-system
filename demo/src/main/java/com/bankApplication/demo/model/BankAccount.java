package com.bankApplication.demo.model;

import com.bankApplication.demo.advice.InsufficientFundsException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column
    private boolean Active = true;


    @Column
    @Enumerated(EnumType.STRING)
    private AccountType accountType;






    // *************** CONSTRUCTORS ***********************


    public BankAccount(double balance, AccountType accountType) {
        this.balance = balance;
        this.accountType = accountType;
    }

    public void deposit(double amount){
        if (amount<=0){
            throw new IllegalArgumentException("Deposit must be positive");
        }

        balance+=amount;

    }




    public void withdraw(double amount){
        if (amount<=0){
            throw new IllegalArgumentException("Withdrawal must be positive");
        }
        if (amount>balance){
            throw new InsufficientFundsException();
        }else {
            balance-=amount;

        }
    }







}
