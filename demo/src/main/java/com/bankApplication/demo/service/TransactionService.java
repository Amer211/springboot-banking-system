package com.bankApplication.demo.service;

import com.bankApplication.demo.model.BankAccount;
import com.bankApplication.demo.model.Transaction;
import com.bankApplication.demo.model.TransactionType;
import com.bankApplication.demo.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;



    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;

    }


    // ******************** create transaction ***************

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Transaction createTransaction(BankAccount account,
                                         BigDecimal amount,
                                         TransactionType type){

        Transaction transaction = new Transaction(account,
                amount,type);

        return transactionRepository.save(transaction);

    }



    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markCompleted(Long id){
        Transaction transaction = transactionRepository.findById(id)
                        .orElseThrow();
        transaction.markCompleted();
        transactionRepository.save(transaction);
    }





    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(Long id){
        Transaction transaction = transactionRepository.findById(id)
                        .orElseThrow();
        transaction.markFailed();
        transactionRepository.save(transaction);
    }


    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Transaction with id: " + id + " not found"));
    }

    public Transaction getTransactionByReferenceId(String referenceId) {
        return transactionRepository.findByReferenceId(referenceId)
                .orElseThrow(()-> new IllegalArgumentException("Transaction with reference id: " + referenceId + " not found"));
    }
}
