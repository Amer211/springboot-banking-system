package com.bankApplication.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor  // don't use AllArgs in entities
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column
    private LocalDateTime createdAt;

    private double amount;


    private String referenceId;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private BankAccount account;



    public Transaction(BankAccount account, double amount) {
        this.account = account;
        this.amount=amount;
        this.status = TransactionStatus.PENDING;
        this.referenceId = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markCompleted(){
        if (this.status!=TransactionStatus.PENDING){
            throw new IllegalArgumentException("Illegal status transition");
        }
        this.status = TransactionStatus.COMPLETED;

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(){
        if(this.status!=TransactionStatus.PENDING){
            throw new IllegalArgumentException("Only PENDING can become FAILED");
        }
        this.status= TransactionStatus.FAILED;
    }








}
