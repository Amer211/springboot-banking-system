package com.bankApplication.demo.model;

import com.bankApplication.demo.advice.InsufficientFundsException;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private BigDecimal balance;

    @Setter
    @Column
    private String accountNumber;


    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL,orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private List<Transaction> transactions = new ArrayList<>();



    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Setter
    @Column
    private boolean active = true;


    @Column
    @Enumerated(EnumType.STRING)
    private AccountType accountType;






    // *************** CONSTRUCTORS ***********************

    public BankAccount(BigDecimal balance, AccountType accountType) {
        this.balance = balance;
        this.accountType = accountType;
    }




    public void deposit(BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Deposit must be positive");
        }

        this.balance=this.getBalance().add(amount);

    }




    public void withdraw(BigDecimal amount){
        if (amount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Withdrawal must be positive");
        }
        if (amount.compareTo(this.balance)>0){
            throw new InsufficientFundsException();
        }
        this.balance=this.balance.subtract(amount);


    }


    public void transfer(BankAccount destinationAccount, BigDecimal amount){

        if(this.accountNumber.equals(destinationAccount.accountNumber)){
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        if(amount.compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        this.withdraw(amount);
        destinationAccount.deposit(amount);


    }










}
